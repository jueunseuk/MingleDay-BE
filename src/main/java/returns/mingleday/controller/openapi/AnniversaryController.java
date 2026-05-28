package returns.mingleday.controller.openapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import returns.mingleday.model.anniversary.AnniversaryItem;
import returns.mingleday.service.openapi.AnniversaryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/anniversary")
public class AnniversaryController {

    private final AnniversaryService anniversaryService;

    @GetMapping
    public ResponseEntity<List<AnniversaryItem>> getAnniversary(
            @RequestParam Integer year,
            @RequestParam Integer month
    ) {
        List<AnniversaryItem> responses = anniversaryService.getAnniversary(year, month);
        return ResponseEntity.ok(responses);
    }
}
