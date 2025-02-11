# Spring @Primary 애너테이션

## @Primary란?
`@Primary`는 스프링에서 같은 타입의 빈이 여러 개 있을 때, 기본적으로 주입할 빈을 지정하는 애너테이션입니다.

## 설명
스프링 컨테이너가 빈을 주입할 때 같은 타입의 빈이 여러 개 존재하면, 어떤 빈을 선택할지 모호성이 발생합니다. `@Primary` 애너테이션을 사용하면 특정 빈을 우선적으로 주입하도록 설정할 수 있습니다.

## 사용 예시
```java
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

interface Printer {
    void print();
}

@Component
class ConsolePrinter implements Printer {
    @Override
    public void print() {
        System.out.println("콘솔 프린터");
    }
}

@Primary
@Component
class FilePrinter implements Printer {
    @Override
    public void print() {
        System.out.println("파일 프린터");
    }
}

@Component
class PrintService {
    private final Printer printer;

    public PrintService(Printer printer) {
        this.printer = printer;
    }

    public void doPrint() {
        printer.print();
    }
}
```

## 실행 결과
위 코드에서 `FilePrinter` 클래스에 `@Primary` 애너테이션이 지정되어 있기 때문에 `PrintService`는 기본적으로 `FilePrinter`를 주입받게 됩니다.

## 정리
- 같은 타입의 빈이 여러 개 있을 때 기본적으로 사용할 빈을 지정할 수 있다.
- `@Primary`를 사용하면 `@Autowired` 시 우선적으로 선택된다.
- 특정 빈을 명확하게 지정하고 싶다면 `@Qualifier`를 함께 사용할 수도 있다.

