package co.yixiang.modules.webs.rest;

import co.yixiang.annotation.AnonymousAccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class helloController {

    @GetMapping(value = "/sayHello")
    @AnonymousAccess
    public ResponseEntity sayHello(){
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }
}
