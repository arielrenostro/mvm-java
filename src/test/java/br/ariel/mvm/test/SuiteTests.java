package br.ariel.mvm.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ariel.mvm.test.montador.MontadorMVMTest;

/**
 * @author ariel
 */
@RunWith(Suite.class)
@SuiteClasses({MemoriaTest.class, //
	ProcessadorTest.class, //
	BIOSTest.class, //
	MontadorMVMTest.class })
public class SuiteTests {

}
