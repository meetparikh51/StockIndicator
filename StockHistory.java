import java.util.Map;

public class StockHistory {

  private String date;
  private double open;
  private double close;
  private double high;
  private double low;
  private long volume;
  private double adjClose;
  private Map<StockADX,Double> stockADXFields;
  
  public StockHistory(String line) {
    // Date, Open,  High, Low, Close, Volume, Adj Close
   /* if (line.startsWith("\"")) {
      line = line.substring(1);
    }
    if (line.endsWith("\"")) {
      line = line.substring(line.length() - 1);
    }*/
    String[] lineData = line.split(",");
    setDate(lineData[0]);
    setOpen(Double.parseDouble(lineData[1]));
    setClose(Double.parseDouble(lineData[4]));
    setHigh(Double.parseDouble(lineData[2]));
    setLow(Double.parseDouble(lineData[3]));
    setVolume(Long.parseLong(lineData[5]));
    setAdjClose(Double.parseDouble(lineData[6]));
  }
  public String getDate() {
    return date;
  }
  public void setDate(String date) {
    this.date = date;
  }
  public double getOpen() {
    return open;
  }
  public void setOpen(double open) {
    this.open = open;
  }
  public double getClose() {
    return close;
  }
  public void setClose(double close) {
    this.close = close;
  }
  public double getHigh() {
    return high;
  }
  public void setHigh(double high) {
    this.high = high;
  }
  public double getLow() {
    return low;
  }
  public void setLow(double low) {
    this.low = low;
  }
  public long getVolume() {
    return volume;
  }
  public void setVolume(long volume) {
    this.volume = volume;
  }
  public double getAdjClose() {
    return adjClose;
  }
  public void setAdjClose(double adjClose) {
    this.adjClose = adjClose;
  }
public Map<StockADX, Double> getStockADXFields() {
	return stockADXFields;
}
public void setStockADXFields(Map<StockADX, Double> stockADXFields) {
	this.stockADXFields = stockADXFields;
}
  
  
  
  
}
