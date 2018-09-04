package br.ariel.mvm.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author ariel
 */
@RunWith(Suite.class)
@SuiteClasses({ MemoriaTest.class, //
		MonitorTest.class, //
		ProcessadorTest.class, //
		BIOSTest.class })
public class SuiteTests {

}
