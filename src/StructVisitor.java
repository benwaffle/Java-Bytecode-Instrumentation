import org.objectweb.asm.*;

public class StructVisitor extends ClassVisitor implements Opcodes {
	private boolean makePublic = false;

	public StructVisitor(ClassVisitor cv) {
		super(ASM5, cv);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		cv.visit(version, ACC_PUBLIC, name, signature, superName, interfaces);
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		makePublic = desc.equals("LStruct;");
		return makePublic ? cv.visitAnnotation(desc, visible) : null;
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		return makePublic ?
				cv.visitField(ACC_PUBLIC, name, desc, signature, value) : // if annotation is set, make field public
					null;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		return name.equals("<init>") ? cv.visitMethod(ACC_PUBLIC, name, desc,
				signature, exceptions) : null;
	}
}
