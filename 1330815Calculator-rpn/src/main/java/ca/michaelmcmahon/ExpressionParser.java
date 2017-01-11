package ca.michaelmcmahon;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/***
 * 
 * @author Michael McMahon
 * @version 1.0
 * 
 * This Class is responsible of taking a mathematical expression contained in a a queue and solves
 * the equation using the reverse polish notation. The algorithm works with both parentheses and
 * floating point numbers. 
 * 
 * if the expression is not in a valid form, it will throw a InfixQueueIllegalStateException.
 */
public class ExpressionParser 
{
	private ArrayDeque<String> operandStack;
	private ArrayDeque<String> operatorStack;
	private Queue<String> infixQueue;
	private Queue<String> postfixQueue;
	private Deque<String> expressionDequeue;
	
	
	/***
	 * Construct a new Expression parser with a  default
	 * infix Queue.
	 * 
	 * @param infixQueue - the mathematical expression.
	 */
	public ExpressionParser(Queue<String> infixQueue)
	{
		if(infixQueue == null)
			throw new IllegalArgumentException("The infix queue passed in cannot be null");
		
		operandStack = new ArrayDeque<String>();
		operatorStack = new ArrayDeque<String>();
		postfixQueue =  new LinkedList<String>();
		expressionDequeue =  new LinkedList<String>();
			
		this.infixQueue = infixQueue;
	}
	/***
	 * Construct a new Expression parser. The mathematical
	 * expression Queue can be set later.
	 */
	public ExpressionParser()
	{
		operandStack = new ArrayDeque<String>();
		operatorStack = new ArrayDeque<String>();
		postfixQueue =  new LinkedList<String>();
		expressionDequeue =  new LinkedList<String>();
	}
	
	/**
	 * Sets the mathematical
	 * expression Queue.
	 * 
	 * @param infix - the mathematical expression
	 */
	public void setInfixQueue(Queue<String> infix)
	{
		operandStack.clear();
		operatorStack.clear();
		postfixQueue.clear();
		expressionDequeue.clear();
		this.infixQueue = infix;
	}
	
	/**
	 * Determines if the operator sent in is either
	 * a + - / *
	 * @param item -  the operator.
	 * 
	 * @return true if it a binary operator.
	 */
	private boolean isQueueItemAnBinaryOperator(String item)
	{
		boolean result = false;
		
		if(item.equals("+") ||
		   item.equals("*") ||
		   item.equals("/") ||
		   item.equals("-"))
		{
			result = true;
		}
		
		return result;
	}
	/**
	 * Determines if the operator sent in is either
	 * a + - / * ( )
	 * @param item -  the operator.
	 * 
	 * @return true if it a binary operator or expression operator.
	 */
	private boolean isQueueItemABinaryOrExpressionOperator(String item)
	{
		boolean result = false;
		
		if(item.equals("+") ||
		   item.equals("*") ||
		   item.equals("/") ||
		   item.equals("-") ||
		   item.equals("(") ||
		   item.equals(")"))
		{
			result = true;
		}
		
		return result;
	}
	
	/**
	 * Determines if the item passed is a valid 
	 * integer number or floating point number.
	 * @param item
	 * @return true if it is a valid number.
	 */
	private boolean IsQueueItemANumber(String item)
	{
		boolean result = true;
		
		char chars[] =  item.toCharArray();
		int charsLength = chars.length;
		
		if(chars[0] == '.' || chars[charsLength -1] == '.')
			return false;
			
		int dotCOunt = 0 ;
		
		for (char c : chars) 
		{
			if(c == '.')
			{
				if(dotCOunt >= 1)
				{
					result = false;
					break;
				}
				else
					dotCOunt++;
			}
			else if(!Character.isDigit(c))
			{
				result = false;
				break;
			}
		}
		
		return result;
	}
	/**
	 * Converts the internal infix mathematical expression queue into a 
	 * postfix queue using reverse polish notation.
	 * 
	 * validation is done as the infix queue is parsed.
	 * 
	 * @throws InfixQueueIllegalStateException - if the infix is not in a valid form.
	 */
	private void convertInfixQueueToPostFixQueue() throws InfixQueueIllegalStateException
	{
				
		String firstItme = infixQueue.peek();
		
		if(isQueueItemAnBinaryOperator(firstItme))
		{
			throw new InfixQueueIllegalStateException("the first item in the queue cannot be an operator");
		}
		int parenthesesCount = 0;
		String lastItem = "";
		
		for (String string : infixQueue) 
		{
			if(string.equals("(") || string.equals(")"))
				parenthesesCount++;
			
			/// checks to make sure that the current queue item is valid.
			if(isQueueItemAnBinaryOperator(string) && isQueueItemAnBinaryOperator(lastItem))
			{
				throw new InfixQueueIllegalStateException("there can not be two operators one after the other.");
			}
			else if(string.equals("(") && (!isQueueItemAnBinaryOperator(lastItem) && !lastItem.equals("") && !lastItem.equals("(")))
			{
				throw new InfixQueueIllegalStateException("there cannot be a number before an opening parenthesis.");
			}
			else if(lastItem.equals("(") && (isQueueItemAnBinaryOperator(string) && !string.equals(")")))
			{
				throw new InfixQueueIllegalStateException("there cannot be an operator after a opening parenthesis.");
			}
			else if( (string.equals(")") && (isQueueItemAnBinaryOperator(lastItem) && !string.equals("("))))
			{
				throw new InfixQueueIllegalStateException("there cannot be a operator before a closing parenthesis.");
			}
			else if(lastItem.equals(")") && !isQueueItemAnBinaryOperator(string) && !string.equals(")"))
			{
				throw new InfixQueueIllegalStateException("there cannot be a number after a closing parenthesis.");
			}
			else if(!IsQueueItemANumber(string) && !isQueueItemABinaryOrExpressionOperator(string))
			{
				throw new InfixQueueIllegalStateException("A number must consist of charecters consisting of [0,9] and must contain at most one"
						+ " decimal place that cannot be in the front or at the back of a number.");
			}
			
			if(isQueueItemABinaryOrExpressionOperator(string))
			{
				addOperatorToOperaterStack(string);
			}
			else
			{
				postfixQueue.add(string);
			}
			lastItem  = string;
		}
		
		if(parenthesesCount % 2 != 0)
			throw new InfixQueueIllegalStateException("there cannot be an odd amount of parentheses.");
		
		if(!operatorStack.isEmpty())
		{
			int operatorStackLength = operatorStack.size();
			for(int i = 0; i < operatorStackLength; i++)
			{
				postfixQueue.add(operatorStack.pop());
			}
		}
		
	}
	/**
	 * Gives back the postfix queue
	 * @return
	 */
	public Queue<String> getPostFixQueue()
	{
		return postfixQueue;
	}
	/**
	 * Using the reverse polish notation, the method will
	 * add the operator to the stack if it is of a higher precedence
	 * than what is currently on top of the stack.
	 * 
	 * if this is false the the top of the stack is popped off and
	 * added to the postfix queue and the operator passed in will
	 * be added to the top of the stack.
	 * 
	 * @param operator
	 */
	private void addOperatorToOperaterStack(String operator)
	{
		if(operatorStack.isEmpty())
		{
			operatorStack.push(operator);
			return;
		}
		String currentOperator = "";
		while(!operatorStack.isEmpty())
		{
			currentOperator = operatorStack.peek();
			
			if(currentOperator.equals("(") && (!operator.equals("(") && !operator.equals(")")))
			{
				operatorStack.push(operator);
				break;
			}
			// if higher precedence, then just add it
			if( (operator.equals("*") || operator.equals("/") ) && (currentOperator.equals("+") || currentOperator.equals("-")))
			{
				operatorStack.push(operator);
				break;
			}
			// if it is the same precedence ( * /) then replace it with the one on top of the stack.
			else if( (operator.equals("*") || operator.equals("/") ) && (currentOperator.equals("*") || currentOperator.equals("/")) )
			{
				postfixQueue.add(operatorStack.pop());
			}
			// if it is the same precedence ( + -) then replace it with the one on top of the stack.
			else if ( (operator.equals("+") || operator.equals("-") ) && (currentOperator.equals("+") || currentOperator.equals("-")) )
			{
				postfixQueue.add(operatorStack.pop());
			}
			// it is a parentheses just add it
			else if(operator.equals("("))
			{
				operatorStack.push(operator);
				break;
			}
			// if we found a closing parentheses, start popping off the stack
			else if(operator.equals(")"))
			{
				int i = 0;
				while(!operatorStack.peek().equals("("))
				{
					i++;
					postfixQueue.add(operatorStack.pop());
				}
				// pop off the opening parentheses
				operatorStack.pop();
				break;
			}
			// if its less precedence than a * / then replace it with the one on top of the stack.
			else
			{
				postfixQueue.add(operatorStack.pop());
			}
			// if  the stack is empty then we know its ok to add the last operator in.
			if(operatorStack.isEmpty())
			{
				operatorStack.push(operator);
				break;
			}
		}
	}
	
	/**
	 * Now that the expression is in a valid postfix form
	 * the expression can be solved.
	 * 
	 * the first item in the operandstack will contain
	 * the result of the expression.
	 * 
	 * @param postfix
	 * @throws InfixQueueIllegalStateException
	 */
	private void solvePostFixExpression(Queue<String> postfix) throws InfixQueueIllegalStateException
	{
		String tmp = "";
		for (String string : postfix) 
		{
			if(!isQueueItemAnBinaryOperator(string))
			{
				operandStack.push(string);
			}
			else
			{
				expressionDequeue.push(operandStack.pop());
				expressionDequeue.push(string);
				expressionDequeue.push(operandStack.pop());
				operandStack.push(calculateSubExpression(expressionDequeue));
				expressionDequeue.clear();
			}
		}
	}
	
	/**
	 * Using the deque that part of the expression can be calculated.
	 * since the dequeue contains two operand and a operator.
	 * 
	 * @param expression
	 * @return the result of the expression.
	 * 
	 * @throws InfixQueueIllegalStateException
	 */
	private String calculateSubExpression(Deque<String> expression) throws InfixQueueIllegalStateException
	{	
		String result = "";

		BigDecimal operand1 = new BigDecimal(expression.pop());
		String operator = expression.pop();
		BigDecimal operand2 =  new BigDecimal(expression.pop());
		
		switch (operator) 
		{
		case "+":
			result += (operand1.add(operand2));
			break;
		case "-":
			result += (operand1.subtract(operand2));
			break;
		case "/":
			if(operand2.doubleValue() == 0)
				throw new InfixQueueIllegalStateException("you cannot divide by zero");
			result += (operand1.divide(operand2,2, RoundingMode.HALF_UP));
			break;
		case "*":
			result += (operand1.multiply(operand2));
			break;
		}	
		return result;
	}
	
	/**
	 * Retrieves the answer to the mathematical expression.
	 * @return -  the answer.
	 * @throws InfixQueueIllegalStateException
	 */
	public BigDecimal getCalculatedExpression() throws InfixQueueIllegalStateException
	{
		convertInfixQueueToPostFixQueue();
		solvePostFixExpression(postfixQueue);
		return new BigDecimal(operandStack.pop()).stripTrailingZeros();
	}
	
	
}
