package returns.mingleday.service.openapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import returns.mingleday.model.anniversary.AnniversaryApiResponse;
import returns.mingleday.model.anniversary.AnniversaryItemWithType;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnniversaryService {

    @Value("${anniversary.endpoint}")
    private String endpoint;

    @Value("${anniversary.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<AnniversaryItemWithType> getAnniversary(Integer year, Integer month) {
        if(year == null) {
            year = LocalDate.now().getYear();
        }
        if(month == null) {
            month = LocalDate.now().getMonthValue();
        }

        log.info("Anniversary request occurred - year:{}, month:{}", year, month);

        String url = endpoint
                        + "?serviceKey=" + secretKey
                        + "&solYear=" + year
                        + "&solMonth=" + String.format("%02d", month);

        log.info("Anniversary url: {}", url);

        AnniversaryApiResponse response =
                restTemplate.getForObject(
                        url,
                        AnniversaryApiResponse.class
                );

        if(response == null || response.getBody() == null || response.getBody().getItems() == null) {
            return Collections.emptyList();
        }

        return response.getBody()
                .getItems()
                .getItem()
                .stream()
                .map(AnniversaryItemWithType::new)
                .toList();
    }
}
