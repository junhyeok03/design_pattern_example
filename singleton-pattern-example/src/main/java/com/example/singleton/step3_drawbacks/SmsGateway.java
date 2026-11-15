package com.example.singleton.step3_drawbacks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 싱글톤 패턴으로 만든 문자 발송기.
 * 프로그램 전체에 하나뿐이라, 보낸 기록도 프로그램 전체가 함께 쓴다.
 */
public class SmsGateway implements MessageGateway {

    private static final SmsGateway INSTANCE = new SmsGateway();

    private final List<String> sent = Collections.synchronizedList(new ArrayList<>());

    private SmsGateway() {
    }

    public static SmsGateway getInstance() {
        return INSTANCE;
    }

    @Override
    public void send(String to, String text) {
        sent.add(to + ": " + text);
    }

    @Override
    public List<String> sent() {
        return List.copyOf(sent);
    }
}
