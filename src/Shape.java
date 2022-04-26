public class Shape {
    public void erase() {
        System.out.println("Cтирать");
    }

    public void draw() {
        System.out.println("Рисовать");
    }

    void doSomething(Shape shape) {
        shape.erase();
        shape.draw();
    }
}
