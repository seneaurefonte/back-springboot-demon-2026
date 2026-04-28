# 🔐 Manuel de Procédure : Mise en place de l'Authentification JWT

Ce manuel détaille les étapes techniques pour implémenter un système d'authentification robuste et stateless utilisant **Spring Security** et **JSON Web Tokens (JWT)**.

---

## 🚀 Concepts Clés de Spring Security

Avant d'entamer l'implémentation, il est essentiel de comprendre les piliers de Spring Security :

| Concept | Description |
| :--- | :--- |
| **Authentication** | Le processus de vérification de **qui** est l'utilisateur (identifiant/mot de passe). |
| **Authorization** | Le processus de vérification de **ce que** l'utilisateur a le droit de faire (Rôles/Permissions). |
| **SecurityFilterChain** | Une chaîne de filtres que chaque requête doit traverser. C'est ici que l'on définit les règles de sécurité. |
| **SecurityContextHolder** | C'est l'endroit où Spring Security stocke les détails de l'utilisateur actuellement authentifié. |
| **AuthenticationManager** | Le composant qui coordonne le processus d'authentification. |
| **UserDetailsService** | Interface utilisée pour charger les données de l'utilisateur (depuis une DB, LDAP, etc.). |
| **GrantedAuthority** | Représente une permission ou un rôle accordé à l'utilisateur (ex: `ROLE_ADMIN`). |
| **PasswordEncoder** | Service utilisé pour hasher les mots de passe et les comparer de manière sécurisée. |

### Comment ils interagissent ?
Lorsqu'une requête arrive, elle passe par le **SecurityFilterChain**. Si c'est une tentative de login, l'**AuthenticationManager** utilise le **UserDetailsService** pour récupérer l'utilisateur et le **PasswordEncoder** pour vérifier le mot de passe. Si tout est correct, l'utilisateur est stocké dans le **SecurityContextHolder**, et ses **GrantedAuthority** sont utilisées pour l'**Authorization** sur les routes suivantes.

#### Flux interne d'Authentification :
```mermaid
sequenceDiagram
    participant Filter as SecurityFilter
    participant Manager as AuthenticationManager
    participant Provider as DaoAuthenticationProvider
    participant Service as UserDetailsService
    participant Encoder as PasswordEncoder

    Filter->>Manager: authenticate(token)
    Manager->>Provider: authenticate(token)
    Provider->>Service: loadUserByUsername(email)
    Service-->>Provider: UserDetails
    Provider->>Encoder: matches(raw, encoded)
    Encoder-->>Provider: boolean (true/false)
    Provider-->>Manager: Authentication (Success)
    Manager-->>Filter: Authentication (Success)
    Filter->>Filter: SecurityContextHolder.set(auth)
```

#### Flux de Contrôle d'Accès (Authorization) :
```mermaid
sequenceDiagram
    participant Request as Client Request
    participant Filter as AuthorizationFilter
    participant Context as SecurityContextHolder
    participant Controller as Resource Controller

    Request->>Filter: Request to /api/patients
    Filter->>Context: getAuthentication()
    Context-->>Filter: Principal with Roles [ROLE_ADMIN]
    
    alt A le rôle requis
        Filter->>Controller: Autoriser l'accès
        Controller-->>Request: 200 OK (Data)
    else Pas de rôle requis
        Filter-->>Request: 403 Forbidden
    end
```

---

## 1. Architecture Globale
L'authentification repose sur un système de filtrage qui intercepte chaque requête pour valider l'identité de l'utilisateur.

```mermaid
graph TD
    Client[Client / Mobile / Web] -->|1. POST /login| AuthController
    AuthController -->|2. Valider| AuthService
    AuthService -->|3. Rechercher| DB[(Base de Données)]
    AuthService -->|4. Générer Token| JWT[JwtTokenProvider]
    JWT -->|5. Retourne Token| Client
    
    Client -->|6. Requête avec Bearer Token| Filter[JwtAuthenticationFilter]
    Filter -->|7. Valider Token| JWT
    Filter -->|8. Charger User| UserDetails[CustomUserDetailsService]
    Filter -->|9. Injecter Contexte| SecurityContext[SecurityContextHolder]
    SecurityContext -->|10. Autoriser| Controller[API Controller]
```

---

## 2. Diagramme de Séquence : Authentification (Login)
Voici le flux détaillé lors de la connexion d'un utilisateur.

```mermaid
sequenceDiagram
    participant Client
    participant Controller as AuthController
    participant Service as AuthService
    participant Provider as JwtTokenProvider
    participant DB as UserRepository

    Client->>Controller: POST /api/auth/login (email, password)
    Controller->>Service: login(dto)
    Service->>DB: findByEmail(email)
    DB-->>Service: UserEntity
    Service->>Service: checkPassword(hash)
    
    alt Identifiants Valides
        Service->>Provider: generateToken(user)
        Provider-->>Service: JWT Token
        Service-->>Controller: LoginResponseDTO (token)
        Controller-->>Client: 200 OK (RestResponse)
    else Identifiants Invalides
        Service-->>Controller: throw EntityNotFoundException
        Controller-->>Client: 404 Not Found / 401 Unauthorized
    end
```

---

## 3. Étapes de Mise en Place

### Étape 1 : Modèle de Données (Entities)
L'entité utilisateur doit implémenter `UserDetails` pour être compatible avec Spring Security.

```java
@Entity
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String email;
    protected String password; // Doit être hashé avec BCrypt
    
    @ManyToMany(fetch = FetchType.EAGER)
    protected Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .toList();
    }
}
```

### Étape 2 : Le Fournisseur de Token (JwtTokenProvider)
Ce service gère la création et la lecture des tokens via la bibliothèque JJWT.

```java
@Service
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(UserDetails userDetails) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .signWith(key)
                .compact();
    }
}
```

### Étape 3 : Le Filtre de Sécurité (JwtAuthenticationFilter)
Il intercepte les appels pour vérifier si un token est présent dans les headers `Authorization`.

```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String token = getJwtFromRequest(request);
        if (tokenProvider.validateToken(token)) {
            String username = tokenProvider.getUsernameFromToken(token);
            UserDetails user = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
```

### Étape 4 : Configuration de Spring Security (SecurityConfig)
C'est ici que l'on définit les routes publiques et l'injection du filtre JWT.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() 
                .anyRequest().authenticated() 
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
```

---

## 4. Guide d'Utilisation des Tests
Pour tester votre implémentation, utilisez un fichier `.http` ou Postman :

1.  **Login** : Envoyer les identifiants pour recevoir le token.
2.  **Accès Protégé** : Insérer le token dans le header `Authorization: Bearer <votre_token>`.

---

> [!IMPORTANT]
> **Sécurité** : Ne commitez jamais votre `jwt.secret` dans un dépôt public. Utilisez des variables d'environnement ou un gestionnaire de secrets (comme Vault).
