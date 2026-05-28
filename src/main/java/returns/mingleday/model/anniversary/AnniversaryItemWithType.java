package returns.mingleday.model.anniversary;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AnniversaryItemWithType {
    private Integer dateKind;
    private String dateName;
    private Boolean isHoliday;
    private LocalDate locdate;
    private Integer seq;

    public AnniversaryItemWithType(AnniversaryItem anniversaryItem) {
        this.dateKind = Integer.parseInt(anniversaryItem.getDateKind());
        this.dateName = anniversaryItem.getDateName();
        this.isHoliday = anniversaryItem.getIsHoliday().equalsIgnoreCase("Y");
        this.locdate = LocalDate.parse(anniversaryItem.getLocdate());
        this.seq = Integer.parseInt(anniversaryItem.getSeq());
    }
}
