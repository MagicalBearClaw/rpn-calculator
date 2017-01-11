package ca.michaelmcmahon.test;

import static org.junit.Assert.assertEquals;

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
public class CalculatorExceptionTest 
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
   
   public CalculatorExceptionTest(String infix,  String expectedPostfix, BigDecimal expectedResult)
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
    * Different invalid data.
    * @return the collection of parameters 
    */
   @Parameterized.Parameters
   public static Collection<Object[]> Expressions() 
   {
	   Object parametrs[][] = new Object[12][];
	   parametrs[0] = new Object[] {"3+", 
			   						"2 7 + 4 -", new BigDecimal(5)};
	   
	   parametrs[1] = new Object[] {"2 + 7 + * 5", 
			   					    "2 7 + 1 5 * +", new BigDecimal(14)};
	   
	   parametrs[2] = new Object[] {"+2 + 7 + 1 * 5 / 1", 
			   						"2 7 + 1 5 * 1 / +", new BigDecimal(14)};
	   
	   parametrs[3] = new Object[] {"2 / 2 + + 7 + 1 * 5 / 1", 
			   						"2 2 / 7 + 1 5 * 1 / +", new BigDecimal(13)};
	   
	   parametrs[4] = new Object[] {"(2 / 2 + 7 + 1 * 5 / 1 * 13 + 1", 
			   						"2 2 / 7 + 1 5 * 1 / 13 * + 1 +", new BigDecimal(74)};
	   
	   parametrs[5] = new Object[] {"(5 * 3) * 2 + 1)", 
			   					    "5 3 * 2 * 1 +", new BigDecimal(31)};
	   
	   parametrs[6] = new Object[] {"a + b * c ", 
				    "5 3 * 2 * 1 +", new BigDecimal(31)};
	   
	   parametrs[7] = new Object[] {"1 / 0 ", 
			    "5 3 * 2 * 1 +", new BigDecimal(31)};
	   
	   parametrs[8] = new Object[] {".22 + 7 + 1 * 5 / 1", 
					"2 7 + 1 5 * 1 / +", new BigDecimal(14)};
	   parametrs[9] = new Object[] {"2 + 7 + 1 * 5 / 1.", 
					"2 7 + 1 5 * 1 / +", new BigDecimal(14)};
	   parametrs[10] = new Object[] {"2.2.2 + 7 + 1 * 5 / 1", 
					"2 7 + 1 5 * 1 / +", new BigDecimal(14)};
	   parametrs[11] = new Object[] {"2.2.2 + 7 + 1 * 5 / 1", 
				"2 7 + 1 5 * 1 / +", new BigDecimal(14)};
	   
	   return Arrays.asList(parametrs);
   }
   
   /**
    * Given invalid date test to make sure exceptions are thrown.
    * @throws InfixQueueIllegalStateException
    */
   @Test(expected = InfixQueueIllegalStateException.class)
   public void validExpressionTest() throws InfixQueueIllegalStateException 
   {
	   calc.setInfixQueue(infixQueue);
	   BigDecimal res = calc.getCalculatedExpression();
   }
}