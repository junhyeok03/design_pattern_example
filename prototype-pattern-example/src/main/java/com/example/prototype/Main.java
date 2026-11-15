package com.example.prototype;

import java.util.List;
import java.util.function.Supplier;

/**
 * 넥서스가 웨이브마다 미니언을 내보내는 기능을 단계별로 고쳐 나간다.
 * 단계마다 클래스 이름(Nexus, Minion ...)이 같아서 Nexus는 패키지 이름까지 적어 구분한다.
 */
public class Main {

    public static void main(String[] args) {
        step1();
        System.out.println();
        step2();
        System.out.println();
        step3();
        System.out.println();
        step4();
    }

    private static void step1() {
        System.out.println("===== 1단계: 패턴 없이 (웨이브마다 종류별 if로 new) =====");
        var nexus = new com.example.prototype.step1_nopattern.Nexus();

        timed("웨이브 1", nexus::spawnWave);
        System.out.println("→ 미니언 한 마리마다 모델을 새로 로드한다. 웨이브가 올 때마다 이만큼 걸린다.");
    }

    private static void step2() {
        System.out.println("===== 2단계: clone() (얕은 복사) =====");
        var nexus = timed("게임 시작: 원본 3마리 준비", com.example.prototype.step2_shallowclone.Nexus::new);
        var wave = timed("웨이브 1", nexus::spawnWave);

        System.out.println("애쉬가 근거리 미니언 한 마리에게 둔화를 건다...");
        wave.get(0).applyEffect("둔화");
        wave.forEach(minion -> System.out.println("  " + minion));
        System.out.println("다음 웨이브 첫 미니언: " + nexus.spawnWave().get(0));
        System.out.println("→ 근거리 미니언 전부, 그리고 다음 웨이브 미니언까지 나오자마자 둔화 상태다.");
        System.out.println("→ super.clone()이 상태이상 리스트의 주소만 복사해서, 원본과 복제본이 리스트 하나를 함께 쓴다.");
    }

    private static void step3() {
        System.out.println("===== 3단계: clone() (상태이상 리스트는 깊은 복사) =====");
        var nexus = timed("게임 시작: 원본 3마리 준비", com.example.prototype.step3_deepclone.Nexus::new);
        var wave = timed("웨이브 1", nexus::spawnWave);

        System.out.println("애쉬가 근거리 미니언 한 마리에게 둔화를 건다...");
        wave.get(0).applyEffect("둔화");
        wave.forEach(minion -> System.out.println("  " + minion));
        System.out.println("다음 웨이브 첫 미니언: " + nexus.spawnWave().get(0));
        System.out.println("→ 둔화는 맞은 한 마리에게만 걸렸다.");
        System.out.println("→ 모델은 여전히 공유하는가? " + (wave.get(0).model() == wave.get(1).model()));
    }

    private static void step4() {
        System.out.println("===== 4단계: MinionRegistry (게임 도중 새 종류 등록) =====");
        var nexus = timed("게임 시작: 원본 3마리 등록", com.example.prototype.step4_registry.Nexus::new);
        System.out.println("웨이브 1: " + names(nexus.spawnWave()));

        System.out.println("억제기가 부서졌다!");
        timed("슈퍼 미니언 원본 등록", () -> {
            nexus.onInhibitorDestroyed();
            return null;
        });
        System.out.println("웨이브 2: " + names(nexus.spawnWave()));
        System.out.println("→ 웨이브를 내보내는 spawnWave()는 한 줄도 바뀌지 않았다. 종류 이름만 알고, new도 클래스 이름도 없다.");

        System.out.println();
        System.out.println("바론을 처치했다!");
        timed("강화 미니언 원본 등록", () -> {
            nexus.onBaronBuff();
            return null;
        });
        nexus.spawnWave().forEach(minion -> System.out.println("  " + minion));
        System.out.println("→ 강화 미니언 클래스는 없다. 기존 원본을 복제해 공격력만 올린 객체가 새 원본이 됐다.");
        System.out.println("→ 모델도 다시 로드하지 않아서 0초다. (슈퍼 미니언은 new라서 1초)");
    }

    private static <T> T timed(String label, Supplier<T> action) {
        long start = System.nanoTime();
        T result = action.get();
        System.out.printf("%s: %.1f초%n", label, (System.nanoTime() - start) / 1e9);
        return result;
    }

    private static List<String> names(List<?> wave) {
        return wave.stream().map(minion -> minion.getClass().getSimpleName()).toList();
    }
}
