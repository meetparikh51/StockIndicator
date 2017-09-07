import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AverageDirectionalIndex implements StockAnalyzer {

	private final List<StockHistory> s;
	private static final int STOCK_WINDOW_SIZE = 14;
	

	//constructor
	public AverageDirectionalIndex(List<StockHistory> stockHistorys) {
		Collections.reverse(stockHistorys);
		this.s = stockHistorys;
	}
	
	public void analyze(){
		
		for(int i=1;i<s.size();i++){
			StockHistory stock = s.get(i); 
			Map<StockADX, Double> stockFields = stock.getStockADXFields();
			
			//TR1 Calculation
			double TR = calculateTR(i,s);
			//System.out.println(TR);
			
			if (stockFields == null) {
				stockFields = new HashMap<>();
				stock.setStockADXFields(stockFields);
			}
			if (stockFields != null) {
				stockFields.put(StockADX.TR, TR);
			}
			
			
			//DM1 calculation of both positive and negative
			calculateDM(i,s);
			 
		}
		
		
		//FirstTR14
		double sum =0.0;
		for(int i=1; i<=STOCK_WINDOW_SIZE;i++){
			StockHistory stock = s.get(i);
			Map<StockADX, Double> stockFields1 = stock.getStockADXFields();
			Double TR = stockFields1.get(StockADX.TR);
			sum = sum +TR;
		}
		
		//System.out.println(sum);
		StockHistory stock = s.get(STOCK_WINDOW_SIZE);
		Map<StockADX, Double> stockFields1 = stock.getStockADXFields();
		stockFields1.put(StockADX.TR14, sum);
		
		
		
		//SecondTR14,ThirdTR14...
		for(int i=STOCK_WINDOW_SIZE+1;i<s.size();i++){
			StockHistory stock2 = s.get(i);
			Map<StockADX, Double> stockFields2 = stock2.getStockADXFields();
			Double currentTR = stockFields2.get(StockADX.TR);
			
			StockHistory stock3 = s.get(i-1);
			Map<StockADX, Double> stockFields3 = stock3.getStockADXFields();
			Double previousTR14 = stockFields3.get(StockADX.TR14);
			//System.out.println(previousTR14);
			double TR14 = (previousTR14 - (previousTR14/14)) + currentTR;
			stockFields2.put(StockADX.TR14, TR14);
			//System.out.println(TR14);
		}
		
	
		//FirstDM14+
		double sum1 =0.0;
		for(int i=1; i<=STOCK_WINDOW_SIZE;i++){
			StockHistory stock4 = s.get(i);
			Map<StockADX, Double> stockFields4 = stock4.getStockADXFields();
			Double DM = stockFields4.get(StockADX.DM1_positive);
			sum1 = sum1 +DM;
		}
		//System.out.println(sum1);
		StockHistory stock4 = s.get(STOCK_WINDOW_SIZE);
		Map<StockADX, Double> stockFields4 = stock.getStockADXFields();
		stockFields1.put(StockADX.DM14_positive, sum1);
		
		
		//SecondDM14+, ThirdDM14+
		for(int i=STOCK_WINDOW_SIZE+1;i<s.size();i++){
			StockHistory stock5 = s.get(i);
			Map<StockADX, Double> stockFields5 = stock5.getStockADXFields();
			Double currentDM = stockFields5.get(StockADX.DM1_positive);
			
			StockHistory stock6 = s.get(i-1);
			Map<StockADX, Double> stockFields6 = stock6.getStockADXFields();
			Double previousDM14 = stockFields6.get(StockADX.DM14_positive);
			//System.out.println(previousDM14);
			double DM14 = (previousDM14 - (previousDM14/STOCK_WINDOW_SIZE)) + currentDM;
			stockFields5.put(StockADX.DM14_positive, DM14);
			//System.out.println(DM14);
		}
		
		
		
		//FirstDM14-
		double sum2 =0.0;
		for(int i=1; i<=STOCK_WINDOW_SIZE;i++){
			StockHistory stock7 = s.get(i);
			Map<StockADX, Double> stockFields7 = stock7.getStockADXFields();
			Double DM = stockFields7.get(StockADX.DM1_negative);
			//System.out.println(DM);
			sum2 = sum2 +DM;
		}
		//System.out.println(sum2);
		StockHistory stock8 = s.get(STOCK_WINDOW_SIZE);
		Map<StockADX, Double> stockFields8 = stock.getStockADXFields();
		stockFields1.put(StockADX.DM14_negative, sum2);
		
		
		//SecondDM14-,ThirdDM14-..
		for(int i=STOCK_WINDOW_SIZE+1;i<s.size();i++){
			StockHistory stock9 = s.get(i);
			Map<StockADX, Double> stockFields9 = stock9.getStockADXFields();
			Double currentDM = stockFields9.get(StockADX.DM1_negative);
			
			StockHistory stock10 = s.get(i-1);
			Map<StockADX, Double> stockFields10 = stock10.getStockADXFields();
			Double previousDM14 = stockFields10.get(StockADX.DM14_negative);
			//System.out.println(previousDM14);
			double DM14 = (previousDM14 - (previousDM14/STOCK_WINDOW_SIZE)) + currentDM;
			stockFields9.put(StockADX.DM14_negative, DM14);
			//System.out.println(DM14);
		}
		
		
		//DI_14_positive
		for(int i=STOCK_WINDOW_SIZE;i<s.size();i++){
			StockHistory stock11 = s.get(i);
			Map<StockADX, Double> stockFields11 = stock11.getStockADXFields();
			double currentDM14 =stockFields11.get(StockADX.DM14_positive);
			double currentTR14 =stockFields11.get(StockADX.TR14);
			
			double DI14_positive= ((currentDM14/currentTR14) * 100);
			//System.out.println(DI14_positive);
			stockFields11.put(StockADX.DI14_positive, DI14_positive);
			
		}
		
		
		
		//DI_14_negative
		for(int i=STOCK_WINDOW_SIZE;i<s.size();i++){
			StockHistory stock11 = s.get(i);
			Map<StockADX, Double> stockFields11 = stock11.getStockADXFields();
			double currentDM14 =stockFields11.get(StockADX.DM14_negative);
			double currentTR14 =stockFields11.get(StockADX.TR14);
			
			double DI14_negative= ((currentDM14/currentTR14) * 100);
			//System.out.println(DI14_negative);
			stockFields11.put(StockADX.DI14_negative, DI14_negative);	
		}
		
		
		//DI14_diff
		for(int i=STOCK_WINDOW_SIZE;i<s.size();i++){
			StockHistory stock12 = s.get(i);
			Map<StockADX, Double> stockFields12 = stock12.getStockADXFields();
			double DI14_positive = stockFields12.get(StockADX.DI14_positive);
			double DI14_negative = stockFields12.get(StockADX.DI14_negative);
			
			double DI14_diff = Math.abs(DI14_positive-DI14_negative);
			//System.out.println(DI14_diff);
			stockFields12.put(StockADX.DI14_diff, DI14_diff);
		}
		
		
		//DI14_sum
		for(int i=STOCK_WINDOW_SIZE;i<s.size();i++){
			StockHistory stock12 = s.get(i);
			Map<StockADX, Double> stockFields12 = stock12.getStockADXFields();
			double DI14_positive = stockFields12.get(StockADX.DI14_positive);
			double DI14_negative = stockFields12.get(StockADX.DI14_negative);
			
			double DI14_sum = DI14_positive+DI14_negative;
			//System.out.println(DI14_sum);
			stockFields12.put(StockADX.DI14_sum, DI14_sum);
			
		}
		
		
		//DX
		for(int i=STOCK_WINDOW_SIZE;i<s.size();i++){
			StockHistory stock12 = s.get(i);
			Map<StockADX, Double> stockFields12 = stock12.getStockADXFields();
			double DI14_diff = stockFields12.get(StockADX.DI14_diff);
			double DI14_sum = stockFields12.get(StockADX.DI14_sum);
			
			double dx = (DI14_diff/DI14_sum) * 100;
			//System.out.println(dx);
			stockFields12.put(StockADX.DX, dx);
		}
		
		
		//FirstADX
		double sum5 = 0.0;
		for(int i=STOCK_WINDOW_SIZE;i<2*STOCK_WINDOW_SIZE;i++){
			StockHistory stock12 = s.get(i);
			Map<StockADX, Double> stockFields12 = stock12.getStockADXFields();
			double dx = stockFields12.get(StockADX.DX);
			sum5 = sum5 + dx;
			
		}
		double firstADX = sum5/STOCK_WINDOW_SIZE;
		//System.out.println(firstADX);
		StockHistory stock12 = s.get(2*STOCK_WINDOW_SIZE);
		Map<StockADX, Double> stockFields12 = stock12.getStockADXFields();
		stockFields12.put(StockADX.ADX, firstADX);
		
		
		//SecondADX,ThirdADX..
		for(int i=2*STOCK_WINDOW_SIZE+1;i<s.size();i++){
			StockHistory stock13 = s.get(i);
			Map<StockADX, Double> stockFields13 = stock13.getStockADXFields();
			double current_dx = stockFields13.get(StockADX.DX);
			//System.out.println(current_dx);
			StockHistory stock14 = s.get(i-1);
			Map<StockADX, Double> stockFields14 = stock14.getStockADXFields();
			double previousADX =stockFields14.get(StockADX.ADX);
			//System.out.println(previousADX);
			double ADX = ((previousADX * (STOCK_WINDOW_SIZE-1)) + current_dx)/STOCK_WINDOW_SIZE;
			//System.out.println(ADX);
			
			stockFields13.put(StockADX.ADX, ADX);
		}
		
		
		
		
}
	public double calculateTR(int startIndex, List<StockHistory> stockHistory){
		
		double high = stockHistory.get(startIndex).getHigh();
		double low = stockHistory.get(startIndex).getLow();
		double close = stockHistory.get(startIndex-1).getClose();
		
		double result = Math.max(Math.max((high - low), Math.abs(high - close)), Math.abs(close - low));
		return result;
}
	public double calculateTR14(int startIndex, List<StockHistory> stockHistory){
		
		
		double sum =0.0;
		StockHistory stock = s.get(startIndex);
		Map<StockADX, Double> stockFields1 = stock.getStockADXFields();
		List<Double> TRlist = new ArrayList<Double>();
		for (int i = startIndex; i < 14; i++) {
			Double TR = stockFields1.get(StockADX.TR);
			sum =sum + TR;
		}
		return sum;
	}
	
	
	public void calculateDM(int startIndex, List<StockHistory> stockHistory){
		StockHistory stock = s.get(startIndex);
		Map<StockADX, Double> stockFields = stock.getStockADXFields();
		
		if (stockFields == null) {
			stockFields = new HashMap<>();
			stock.setStockADXFields(stockFields);
		}
		
		double high = stockHistory.get(startIndex).getHigh();
		double low = stockHistory.get(startIndex).getLow();
		double priorhigh = stockHistory.get(startIndex-1).getHigh();
		double priorlow = stockHistory.get(startIndex-1).getLow();
		//System.out.println(high);
		//System.out.println(low);
		//System.out.println(priorhigh);
		//System.out.println(priorlow);
		
		
		double highDifference = high - priorhigh;
		//System.out.println(highDifference);
		
		double lowDifference = priorlow - low;
		//System.out.println(lowDifference);
		
		if(highDifference > lowDifference){
			if (stockFields != null) {
				stockFields.put(StockADX.DM1_positive,Math.abs(highDifference));
				stockFields.put(StockADX.DM1_negative,0.0);
				
				//System.out.print(stockFields.get(StockADX.DM1_positive));
				//System.out.println("  " +stockFields.get(StockADX.DM1_negative));
				
			}
		}
		
		else
		{
			if (stockFields != null) {
				stockFields.put(StockADX.DM1_positive,0.0);
				stockFields.put(StockADX.DM1_negative,Math.abs(lowDifference));
				
				//System.out.print(stockFields.get(StockADX.DM1_positive));
				//System.out.println("  " +stockFields.get(StockADX.DM1_negative));
			}
		}
		
		
	}
	
	
	public static void main(String[] args) throws FileNotFoundException{
		List<StockHistory> stockHistorys = StockHistoryReaderUtil.readStock("/Users/meetparikh/Documents/workspace/project/src/A.csv");
		AverageDirectionalIndex adx = new AverageDirectionalIndex(stockHistorys);
		adx.analyze();
	}
}
