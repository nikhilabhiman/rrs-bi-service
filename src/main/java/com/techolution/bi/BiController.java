package com.techolution.bi;

import com.techolution.bi.model.BiTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/electronicsafes")
public class BiController {

    @Autowired
    VBIClient vbiClient;

    @GetMapping()
    @SuppressWarnings("unchecked")
    public ResponseEntity<String> entryCallToBI(HttpServletRequest request, @RequestParam(value = "safeguid") String safeguid) {
        System.out.printf(request.getHeader("Authorization"));
        return new ResponseEntity(request.getHeader("Authorization"), HttpStatus.OK);

    }

    @PostMapping("/createtransaction")
    @SuppressWarnings("unchecked")
    public ResponseEntity<BiTransaction> createTrannsaction(@RequestBody BiTransaction biTransaction) {
        System.out.println(biTransaction);
        return new ResponseEntity(biTransaction, HttpStatus.OK);
    }
}

