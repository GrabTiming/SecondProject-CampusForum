package com.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountPrivacyVo {

    private boolean phone  ;

    private boolean email ;

    private boolean wx ;

    private boolean qq ;

    private boolean gender ;

}
