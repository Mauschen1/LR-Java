import java.util.Scanner; // Подключение класса Scanner
public class Palindrome {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in); // Создается новый объект класса Scanner и сохраняется его в переменной. Вызывается конструктор класса с параметром System.in. Считывание идет с потока программы
        String s = scan.next();  // Выполняется метод из только что созданного объекта
        isPalindrome(s); // Вызов метода isPalindrome
    }
    public static String reverseString(String s){
        String r = ""; // Локальная текстовая переменная
        for (int i = s.length() - 1; i >= 0; --i) // Цикл, в котором возвращается длина строки
            r += s.charAt(i); // Возвращение переменной по указанному индексу i
        return r; // Возвращение переменной
    }
    public static boolean isPalindrome(String s) {
        if(s.equals(reverseString(s))) // Проверка значения равенства слова и его обратной версии
        {
            System.out.println("Палиндром"); // Выведение результата на экран
        }
        else{
            System.out.println("Не палиндром"); // Выведение результата на экран
        }
        return s.equals(reverseString(s)); // Возвращение значения
    }
}


