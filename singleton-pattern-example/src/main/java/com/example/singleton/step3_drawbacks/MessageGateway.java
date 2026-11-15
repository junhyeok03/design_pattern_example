package com.example.singleton.step3_drawbacks;

import java.util.List;

/** 문자 발송 창구. */
public interface MessageGateway {

    void send(String to, String text);

    List<String> sent();
}
