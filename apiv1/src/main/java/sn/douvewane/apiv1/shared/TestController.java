package sn.douvewane.apiv1.shared;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/test")
public class TestController {

    @GetMapping
    public ResponseEntity<RestResponse<String>> test() {
        return ResponseEntity.ok(RestResponse.success("L'API est fonctionnelle !", "Test réussi"));
    }
}
