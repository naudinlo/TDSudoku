package model;

public class Food {
	public int x;
	public int y;
	private int quantity;
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void decreaseQuantity()
	{
		setQuantity(quantity-1);
		if(quantity == 0)
		{
			Beings.sparse.removeObjectsAtLocation(this);
			Beings.createNewFoodResource();
		}
	}
}
