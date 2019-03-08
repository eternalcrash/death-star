package ur.model;

/**
 * Simple class to represent a Cost to build something:
 * a material name and its value
 *
 */
public class MaterialCost {
	private String material;
	private int value;
	
	public MaterialCost(String material, int value) {
		this.material = material;
		this.value = value;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "MaterialCost [material=" + material + ", value=" + value + "]";
	}
	
	
}
