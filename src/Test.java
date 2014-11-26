import java.util.Arrays;
import org.objectweb.asm.*;

public class Test {
	public static void main(String[] args) {
		try {
			ClassReader cr = new ClassReader("Data");
			ClassWriter cw = new ClassWriter(0);
			StructVisitor sv = new StructVisitor(cw);
			cr.accept(sv, 0); 												// do bytecode hacking

			Class<?> moddedClass = new MyLoader().defineClass("Data", cw.toByteArray()); 	// create class from modded bytecode
			System.out.println("Fields: " + Arrays.toString(moddedClass.getDeclaredFields())); 	// prove that field is now public
            System.out.println("Methods: " + Arrays.toString(moddedClass.getDeclaredMethods()));
            System.out.println("Constructors: " + Arrays.toString(moddedClass.getDeclaredConstructors()));
            
            try {
                moddedClass.getField("i").get(moddedClass.newInstance());
            } catch (IllegalAccessException e) {
                System.out.println("fail! private fields should have been made public");
                e.printStackTrace();
            }

            try {
                moddedClass.getMethod("f").invoke(moddedClass.newInstance());
            } catch (NoSuchMethodException e){
                System.out.println("method call successfully failed");
            }

		} catch (Exception e){
			e.printStackTrace();
		}
	}
}

class MyLoader extends ClassLoader {
	public Class<?> defineClass(String name, byte[] b) {
		return defineClass(name, b, 0, b.length);
	}
}

@Struct
class Data {
	private int i;
    private String str;

    public void f(){
        System.out.println("failure! @Struct should have removed this method");
    }
}