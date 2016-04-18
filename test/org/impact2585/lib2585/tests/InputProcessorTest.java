package org.impact2585.lib2585.tests;


import org.impact2585.lib2585.InputProcessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Test for the InputProcessor
 */
public class InputProcessorTest {
	private double analogInput1, analogInput2, analogInput3;
	private double firstMotorSpeed;
	private double secondMotorSpeed;
	private double thirdMotorSpeed;
	private boolean digitalInput1, digitalInput2;
	private TestSystem system;
	private InputProcessor <Double> generalProcessor;
	private InputProcessor<Boolean> booleanProcessor;
	
	/**
	 * Sets up the unit test
	 */
	@Before
	public void setUp() {
		system = new TestSystem();
		generalProcessor = new InputProcessor<Double>(){

			/* (non-Javadoc)
			 * @see org.impact2585.lib2585.DoubleInputProcessor#process(java.lang.Object[])
			 */
			@Override
			public double process(Double... input) {
				return input[0];
			}
		};
		
		booleanProcessor = new InputProcessor<Boolean>() {

			/* (non-Javadoc)
			 * @see org.impact2585.lib2585.InputProcessor#process(java.lang.Object[])
			 */
			@Override
			public double process(Boolean... input) {
				if(input[0] && !input[1]) {
					return 1;
				} else if(input[1] && !input[0]) {
					return -1;
				} else {
					return 0;
				}
			}
		};
		digitalInput1 = false;
		digitalInput2 = false;
	}

	/**
	 * Perform unit test on the InputProcessor
	 */
	@Test
	public void test() {
		//tests input processor for the first motor
		analogInput1 = 1;
		system.run();
		Assert.assertTrue(firstMotorSpeed == 1 && secondMotorSpeed == 0 && thirdMotorSpeed == 0);
		
		//tests logic of the input processor for the first motor
		analogInput2 = 1;
		system.run();
		Assert.assertTrue(firstMotorSpeed == 0 && secondMotorSpeed == 0 && thirdMotorSpeed == 0);
		
		//tests logic of the input processor for the second motor using a lambda expression
		analogInput3 = -1;
		analogInput1 = 0;
		system.run();
		Assert.assertTrue(firstMotorSpeed == -1 && secondMotorSpeed == -1 && thirdMotorSpeed == 0);
		
		//tests input processor for the third motor using boolean input
		analogInput2 = 0;
		analogInput3 = 0;
		digitalInput1 = true;
		system.run();
		Assert.assertTrue(firstMotorSpeed == 0 && secondMotorSpeed == 0 && thirdMotorSpeed == 1);
		
		//tests logic of third motor's input processor
		digitalInput2 = true;
		system.run();
		Assert.assertTrue(firstMotorSpeed == 0 && secondMotorSpeed == 0 && thirdMotorSpeed == 0);
	}
	
	/**
	 * A test system to process input from the input processor
	 */
	private class TestSystem implements Runnable{
		
		/** Sets the speed of the first motor using an InputProcessor
		 * @param processor an InputProcessor that takes input from two analog inputs
		 */
		public void setFirstMotorSpeed(InputProcessor<Double>processor) {
			firstMotorSpeed = processor.process(analogInput1, analogInput2);
		}
		
		/** Sets the speed of the second motor using an InputProcessor
		 * @param processor an InputProcessor that takes input from one analog input
		 */
		public void setSecondMotorSpeed(InputProcessor<Double>processor) {
			secondMotorSpeed = processor.process(analogInput3);
		}
		
		/**Sets the speed of the third motor using an InputProcessor
		 * @param processor an InputProcessor that takes input from two boolean inputs
		 */
		public void setThirdMotorSpeed(InputProcessor<Boolean>processor) {
			thirdMotorSpeed = processor.process(digitalInput1, digitalInput2);
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			this.setFirstMotorSpeed((a) -> {
				if(a[0] != 0 && a[1] == 0) {
					return 1;
				} else if(a[0] == 0 && a[1] != 0) {
					return -1;
				} else {
					return 0;
				}
			});
			
			this.setSecondMotorSpeed(generalProcessor);
			this.setThirdMotorSpeed(booleanProcessor);
		}
		
	}

}


