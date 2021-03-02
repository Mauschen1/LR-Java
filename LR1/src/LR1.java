public class LR1 {
    public static void main(String[] args) {
        for (int i = 2; i < 101; i++)  // Повторение цикла в диапазоне от 2 до 99
        {
            if (isPrime(i)) // Условие проверки
                System.out.println(i + " - простое число."); // Выведение результатов работы программы
        }
    }
    public static boolean isPrime(int n) {
        boolean isPrime = true; // Присвоение значения переменной для проверки
            for (int j = 2; j <= n / j; j++) // Перебор чисел в диапазоне от 2 до n
            {
                if ((n % j) == 0) // Если остаток от деления 0, то число не является простым
                    isPrime = false; // Если условие верно, переменная наоборот принимает ложное значение
            }
        return isPrime; // Возвращает значение переменной
    }
}
