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
        this.locdate = LocalDate.of(
                Integer.parseInt(anniversaryItem.getLocdate().substring(0, 2)),
                Integer.parseInt(anniversaryItem.getLocdate().substring(2, 4)),
                Integer.parseInt(anniversaryItem.getLocdate().substring(4, 6))
        );
        this.seq = Integer.parseInt(anniversaryItem.getSeq());
    }
}
