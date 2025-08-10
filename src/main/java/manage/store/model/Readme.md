# 값 객체 생성 규칙
- 반드시 아래 2가지 메서드를 구현한다.
  - boolean equals(): 값 객체의 동등성을 비교하는 메서드
  - String toString(): 값 객체의 문자열 표현을 반환하는 메서드


- 값 객체의 생성자에서는 주입하는 값에 대한 검증이 이루어 져야 한다.
  ```
  public Money(Long amount) {
    if (amount == null || amount < 0) throw new IllegalArgumentException("Amount must be a non-negative value.");

    this.amount = amount;
  }
  ```

- 값 객체의 setter는 새로운 객체를 반환하도록 구현해야 한다.
    ```
    public Money setAmount(Long amount) {
        return new Money(amount);
    }
    ```