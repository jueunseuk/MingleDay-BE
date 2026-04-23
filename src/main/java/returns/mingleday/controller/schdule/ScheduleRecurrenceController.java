package returns.mingleday.controller.schdule;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mingle/{mingleId}/schedules/{scheduleId}/recurrences")
public class ScheduleRecurrenceController {

}
