package kr.ac.chungbuk.harmonize.utility.error;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResult {

    private List<ErrorDetail> fieldErrors;

    private List<ErrorDetail> objectErrors;

}
