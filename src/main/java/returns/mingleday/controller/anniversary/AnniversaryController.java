package returns.mingleday.controller.anniversary;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/anniversary")
public class AnniversaryController {

    @GetMapping
    public ResponseEntity<?> getAnniversary(
            @RequestParam Integer year,
            @RequestParam Integer month
    ) {

    }
}
