package com.example.prototype;

/**
 * 미니언의 3D 모델과 애니메이션이라고 가정한 클래스. 모든 단계가 함께 쓴다.
 *
 * 만드는 데 오래 걸린다(기본 1초). 대신 한 번 만들면 바뀌지 않으니,
 * 복제본끼리 같은 Model을 함께 써도 안전하다.
 */
public final class Model {

    private static long loadMillis = 1000;

    private final String name;

    public Model(String name) {
        this.name = name;
        try {
            Thread.sleep(loadMillis);    // 파일 읽기, 텍스처 로딩 같은 무거운 작업 흉내
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** 테스트에서는 짧게 줄여 쓴다. */
    public static void setLoadMillis(long millis) {
        loadMillis = millis;
    }

    @Override
    public String toString() {
        return name + " 모델";
    }
}
