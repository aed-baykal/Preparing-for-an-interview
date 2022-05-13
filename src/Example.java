public class Example {
    public static void main(String[] args) {

        Circle circle = new Circle();
        Triangle triangle = new Triangle();
        Square square = new Square();

        circle.doSomething(circle); //круг
        triangle.doSomething(triangle); //треугольник
        square.doSomething(square); //квадрат
    }
}
