package ca.michaelmcmahon.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.michaelmcmahon.ExpressionParser;
import ca.michaelmcmahon.InfixQueueIllegalStateException;

@RunWith(Parameterized.class)
public class CalculatorTest 
{
	@Rule
	public MethodLogger methodLogger = new MethodLogger();
	private ExpressionParser calc;
	private BigDecimal expectedResult;
		
	private  Queue<String> infixQueue;
	private Queue<String>  expectedPostfixQueue;
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
	
   @Before
   public void initialize() 
   {
	   calc =  new ExpressionParser();
   }
   
  
   public CalculatorTest(String infix,  String expectedPostfix, BigDecimal expectedResult)
   {
	   this.expectedResult = expectedResult;
	   infixQueue =  new LinkedList<>();
	   expectedPostfixQueue =  new LinkedList<>();
	   String[] infixes =  infix.split(" ");
	   String[] postFixes =  expectedPostfix.split(" ");
	   
	   for(String item : infixes)
	   {
		   infixQueue.add(item);
	   }
	   
	   for(String item : postFixes)
	   {
		   expectedPostfixQueue.add(item);
	   }
	   
   }
   
   /**
    * Allows for the test class to perform a hole bunch of tests using
    * Different valid data.
    * @return the collection of parameters 
    */
   @Parameterized.Parameters
   public static Collection<Object[]> Expressions() 
   {
	   Object parametrs[][] = new Object[27][];
	   parametrs[0] = new Object[] {"2 + 7 - 4", 
			   						"2 7 + 4 -", new BigDecimal(5)};
	   
	   parametrs[1] = new Object[] {"2 + 7 + 1 * 5", 
			   					    "2 7 + 1 5 * +", new BigDecimal(14)};
	   
	   parametrs[2] = new Object[] {"2 + 7 + 1 * 5 / 1", 
			   						"2 7 + 1 5 * 1 / +", new BigDecimal(14)};
	   
	   parametrs[3] = new Object[] {"2 / 2 + 7 + 1 * 5 / 1", 
			   						"2 2 / 7 + 1 5 * 1 / +", new BigDecimal(13)};
	   
	   parametrs[4] = new Object[] {"2 / 2 + 7 + 1 * 5 / 1 * 13 + 1", 
			   						"2 2 / 7 + 1 5 * 1 / 13 * + 1 +", new BigDecimal(74)};
	   
	   parametrs[5] = new Object[] {"5 * 3 * 2 + 1", 
			   					    "5 3 * 2 * 1 +", new BigDecimal(31)};
	   
	   parametrs[6] = new Object[] {"4 / 2 / 2 + 4", 
			   						"4 2 / 2 / 4 +", new BigDecimal(5)};
	   
	   parametrs[7] = new Object[] {"3.3 + 4.5 * 2 + 6.6 / 2", 
			   						"3.3 4.5 2 * + 6.6 2 / +", new BigDecimal(15.6).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros()};
	   
	   parametrs[8] = new Object[] {"4 / 2 + 5.5 * 4 - 20.5 / 2 - 15", 
			   						"4 2 / 5.5 4 * + 20.5 2 / - 15 -", new BigDecimal(-1.25)};
	   
	   parametrs[9] = new Object[] {"( 4 / 2 ) + ( 5 * 4 ) - ( 20 / 2 ) - 15", 
			   						"4 2 / 5 4 * + 20 2 / - 15 -", new BigDecimal(-3)};
	   
	   parametrs[10] = new Object[] {"4 / ( 2 + 5 ) * ( 4 - 20 / 2 ) - 15", 
			   						 "4 2 5 + / 4 20 2 / - * 15 -", new BigDecimal(-18.42).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros()};
	   
	   parametrs[11] = new Object[] {"( 4 / 2 + 5 * 4 - 20 / 2 - 15 )", 
			   						 "4 2 / 5 4 * + 20 2 / - 15 -", new BigDecimal(-3)};
	   
	   parametrs[12] = new Object[] {"4 / 2 + ( 5 * 4 - ( 20 / 2 ) - 15 )", 
			                         "4 2 / 5 4 * 20 2 / - 15 - +", new BigDecimal(-3)};
	   
	   parametrs[13] = new Object[] {"4 / 2 + 5 * 3 + 20 / ( 2 - 15 )", 
			                         "4 2 / 5 3 * + 20 2 15 - / +", new BigDecimal(15.46).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros()};
	   
	   parametrs[14] = new Object[] {"7 + 3", 
			                         "7 3 +", new BigDecimal(10)};
	   
	   parametrs[15] = new Object[] {"5", 
			   						 "5", new BigDecimal(5)};
	   
	   parametrs[16] = new Object[] {"7 - 3", 
			                         "7 3 -", new BigDecimal(4)};
	   
	   parametrs[17] = new Object[] {"3 - 7", 
			                         "3 7 -", new BigDecimal(-4)};
	   
	   parametrs[18] = new Object[] {"1 - 1 * 5",
			                         "1 1 5 * -", new BigDecimal(-4)};
	   
	   parametrs[19] = new Object[] {"( 1 - 1 ) * 7", 
			                         "1 1 - 7 *", new BigDecimal(0)};
	   
	   parametrs[20] = new Object[] {"7 / 1", 
			                         "7 1 /", new BigDecimal(7).stripTrailingZeros()};
	   
	   parametrs[21] = new Object[] {"1 / 7", 
			                         "1 7 /",  new BigDecimal(0.14).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros()};
	   
	   parametrs[22] = new Object[] {"0 + 0", 
			                         "0 0 +", new BigDecimal(0)};
	   
	   parametrs[23] = new Object[] {"0 * 0", 
			                         "0 0 *", new BigDecimal(0)};
	   
	   parametrs[24] = new Object[] {"7 / 1 / 1 * 3 / 2 / 1 * 5 / 4 + 4", 
			                         "7 1 / 1 / 3 * 2 / 1 / 5 * 4 / 4 +", new BigDecimal(17.13).setScale(2, RoundingMode.HALF_UP)};
	   	   
	   parametrs[25] = new Object[] {"( ( ( ( ( 56.9 * 0.09 ) ) ) ) ) / ( ( 3 - 4.4 ) - ( 79 - 0.3 ) / ( 3 / 2.4 ) ) / 0.5 - 2", 
			    "56.9 0.09 * 3 4.4 - 79 0.3 - 3 2.4 / / - / 0.5 / 2 -", new BigDecimal(-2.16).setScale(2, RoundingMode.HALF_UP)};
	   	
	   parametrs[26] = new Object[] {"5 * 3 + ( 4 ) * 2 + 1", 
			    "5 3 * 4 2 * + 1 +", new BigDecimal(24)};
	   
	   	   
	   return Arrays.asList(parametrs);
   }
   
   /**
    * test to make sure that given the infix queue that both the postfix queue
    * and the final answer are equal to the given postfix queue and final
    * answer.
    * 
    * @throws InfixQueueIllegalStateException - needs this to compile.
    */
   @Test
   public void validExpressionTest() throws InfixQueueIllegalStateException 
   {
	   calc.setInfixQueue(infixQueue);
	   BigDecimal res = calc.getCalculatedExpression();
	   Queue<String> post = calc.getPostFixQueue();
	   boolean isPostFixEquals = expectedPostfixQueue.equals(post);
	   String msg = "Test passed";
	   if(!isPostFixEquals)
	   {
		   msg = getPostFIxErrorInfo(post, "Actual");
		   msg += getPostFIxErrorInfo(expectedPostfixQueue, "Expected");
	   }
	   log.debug("expected post fix is: " + isPostFixEquals);
	   boolean areExpectedResultsEqual = expectedResult.toPlainString().equals(res.toPlainString());
	   
	   if(!areExpectedResultsEqual)
	   {
		   msg = "[Actual] " + res + "[Expected] " + expectedResult;
	   }
	   log.debug("values are equal: " + areExpectedResultsEqual);
	   assertEquals(msg,true, isPostFixEquals && areExpectedResultsEqual);
   }

   // prints valuable information if the test fails.
   public String getPostFIxErrorInfo(Queue<String> post, String tag)
   {
	   String result = "";
	   for(String s : post)
	   {
		   result += s + " ";
	   }
	   
	   return "[" + tag + "] " + result + " ";
   }
}
