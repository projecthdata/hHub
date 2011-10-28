package org.projecthdata.social.api;

import java.io.File;

import org.junit.*;
import static org.junit.Assert.*;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.core.io.ClassPathResource;

public class RootTest {
	private Serializer serializer = new Persister();
	private File source = null;
	private Root root = null;

	@Before
	public void setUp() throws Exception {
		serializer = new Persister();
        source = new ClassPathResource("../resources/root.xml", getClass()).getFile();
		assertTrue(source.exists());
		root = serializer.read(Root.class, source);
		assertNotNull(root);
	}



	@Test
	public void testExtensionsSize(){
		assertEquals(5, root.getExtensions().size());
	}

	@Test
	public void testSectionsSize(){
		assertEquals(4, root.getSections().size());
	}

	@Test
	public void testExtensionIdMap(){
		Extension extension = root.getExtensionById("1");
		assertNotNull(extension);
		assertEquals("application/xml", extension.getContentType());
		assertEquals("http://projecthdata.org/hdata/schemas/2009/06/medication", extension.getContent().trim());

	}


	@Test
	public void testSectionWithChildren(){
		Section section = root.getSections().get(2);
		assertEquals("2", section.getExtensionId());
		assertEquals("Patient Information", section.getName());
		assertEquals("patientinformation", section.getPath());
		assertEquals(1, section.getSections().size());
	}


	@Test
	public void testSectionChildrenPath(){
		Section section = root.getSections().get(2).getSections().get(0);
		assertEquals("patientinformation/photos", section.getPath());
	}


}

