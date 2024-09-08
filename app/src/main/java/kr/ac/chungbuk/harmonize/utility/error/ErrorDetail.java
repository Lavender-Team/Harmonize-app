package kr.ac.chungbuk.harmonize.utility.error;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ErrorDetail {

    private String objectName;
    private String field;
    private String code;
    private String message;

}
