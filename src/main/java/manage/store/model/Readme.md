# 값 객체 생성 규칙
- 반드시 아래 2가지 메서드를 구현한다.
  - boolean equals(): 값 객체의 동등성을 비교하는 메서드
  - String toString(): 값 객체의 문자열 표현을 반환하는 메서드


- 값 객체의 생성자에서는 주입하는 값에 대한 검증이 이루어 져야 한다.
  ```
  public Money(Long amount) {
    if (amount == null || amount < 0) throw new InvalidParameterException("Amount must be a non-negative value.");

    this.amount = amount;
  }
  ```

- 값 객체의 setter는 새로운 객체를 반환하도록 구현해야 한다.
    ```
    public Money setAmount(Long amount) {
        return new Money(amount);
    }
    ```
  
- 값 getter의 이름은 value로 통일한다.
    ```
    public Long value() {
        return amount;
    }
    ```
  
- 최종 Template Method
    ```
    public class {ClassName} {
    
        private {ValueType} value;
    
        public {ClassName}({ValueType} value) {
            if(!{ValueValidationMethod}) throw new InvalidParameterException({errorMsg});
    
            this.value = value;
        }
    
        public String value() {
            return value;
        }
    
        public {ClassName} setValue({ValueType} value) {
            return new {ClassName}(value);
        }
    
        @Override
        public String toString() {
            return value;
        }
    
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
    
            {ClassName} that = ({ClassName}) o;
    
            return {value comparision logic};
        }
    
    }
    ```