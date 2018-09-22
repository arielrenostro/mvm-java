package br.ariel.mvm.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ariel.mvm.test.compilador.CompiladorAssemblyMVMTest;

/**
 * @author ariel
 */
@RunWith(Suite.class)
@SuiteClasses({MemoriaTest.class, //
	ProcessadorTest.class, //
	BIOSTest.class, //
	CompiladorAssemblyMVMTest.class })
public class SuiteTests {

}
