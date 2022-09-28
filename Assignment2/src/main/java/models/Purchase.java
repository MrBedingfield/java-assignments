package models;

import java.util.List;
import java.util.Locale;

public class Purchase {
    private final Product product;
    private int count;

    public Purchase(Product product, int count) {
        this.product = product;
        this.count = count;
    }

    /**
     * parses purchase summary information from a textLine with format: barcode, amount
     *
     * @param textLine
     * @param products a list of products ordered and searchable by barcode
     *                 (i.e. the comparator of the ordered list shall consider only the barcode when comparing products)
     * @return a new Purchase instance with the provided information
     * or null if the textLine is corrupt or incomplete
     */
    public static Purchase fromLine(String textLine, List<Product> products) {
        Purchase newPurchase = null;

        // TODO convert the information in the textLine to a new Purchase instance
        //  use the products.indexOf to find the product that is associated with the barcode of the purchase
        // Read the text line and put the fields into a string array
        String[] fields = textLine.split(",");

        long barcode = Long.parseLong(fields[0].trim());
        int amount = Integer.parseInt(fields[1].trim());

        int productIndex = -1;

        // Loop to find out if the product exists
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getBarcode() == barcode) {
                productIndex = i;
                break;
            }
        }

        if (productIndex < 0) return null;

        Product product = products.get(productIndex);

        newPurchase = new Purchase(product, amount);

        return newPurchase;
    }

    /**
     * add a delta amount to the count of the purchase summary instance
     *
     * @param delta
     */
    public void addCount(int delta) {
        this.count += delta;
    }

    public long getBarcode() {
        return this.product.getBarcode();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    // TODO add public and private methods as per your requirements

    public double getRevenue() {
        return count * product.getPrice();
    }

    @Override
    public String toString() {
        // Purchases shall be displayed with format:barcode/product.title/count/revenue
        return String.format(Locale.ENGLISH, "%d/%s/%d/%.2f", getBarcode(), product.getTitle(), count, getRevenue());
    }
}
