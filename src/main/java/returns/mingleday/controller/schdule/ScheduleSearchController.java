package returns.mingleday.controller.schdule;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import returns.mingleday.service.schedule.ScheduleSearchService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleSearchController {

    private final ScheduleSearchService scheduleSearchService;

}
