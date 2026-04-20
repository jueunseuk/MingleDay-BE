package returns.mingleday.domain.image;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import returns.mingleday.global.domain.BaseTime;
import returns.mingleday.response.code.ImageExceptionCode;
import returns.mingleday.response.exception.BaseException;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class Image extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "origin_name")
    private String originName;

    @Column(name = "stored_name")
    private String storedName;

    @Column(name = "path")
    private String path;

    @Column(name = "size")
    private Long size;

    @Column(name = "target_id")
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type")
    private ImageType imageType;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public static Image of(String originName, String storedName, String path, Long size, Long targetId, ImageType imageType) {
        return Image.builder()
                .originName(originName)
                .storedName(storedName)
                .path(path)
                .size(size)
                .targetId(targetId)
                .imageType(imageType)
                .build();
    }

    public static void validSize(Long size) {
        if(size > MAX_FILE_SIZE) {
            throw new BaseException(ImageExceptionCode.IMAGE_SIZE_EXCEEDED);
        }
    }
}