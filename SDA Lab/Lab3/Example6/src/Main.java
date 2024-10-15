public class Main {
    public static void main(String[] args) {
        //Overloading
        Calculator cal = new Calculator();
        System.out.println(cal.add(12,8));
        System.out.println(cal.add(123456789,987654314));

        ////////////////////////////
        System.out.println();
        ////////////////////////////
        Animal animal = new Animal();
        animal.sound();
        Dog dog = new Dog();
        dog.sound();
        //Overloading
        Animal newDog = new Dog();
        newDog.sound();
    }
}