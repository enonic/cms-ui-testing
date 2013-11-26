package com.enonic.autotests.facets;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.model.site.Portlet;
import com.enonic.autotests.model.site.STKResource;
import com.enonic.autotests.model.site.SectionMenuItem;
import com.enonic.autotests.model.site.Site;
import com.enonic.autotests.model.site.Site.AllowedPageTypes;
import com.enonic.autotests.pages.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.pages.adminconsole.site.SiteMenuItemsTablePage;
import com.enonic.autotests.pages.adminconsole.site.SitePortletsTablePage;
import com.enonic.autotests.pages.adminconsole.site.SitesTableFrame;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.services.SiteService;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.utils.TestUtils;

public class FacetsTests extends BaseTest
{
	private static final String DS_FACET_TERMS = "test-data/facets-ctypes/terms-lastname-ds.xml";
	private static final String DS_FACET_RANGES = "test-data/facets-ctypes/ranges-balance-ds.xml";
	private static final String DS_FACET_HISTOGRAM = "test-data/facets-ctypes/histogram-balance-ds.xml";
	private static final String DS_FACET_DATEHISTOGRAM = "test-data/facets-ctypes/datehistogram-birthday-ds.xml";
	
	private static final String PORTLET_FACET_KEY = "portlet_facet_key";
	private static final String IMPORT_CATEGORY_KEY = "import_cat_key";
	
	
	private String CNAME = "facets-persons";
	private String SITE_FACET_KEY = "facets_site_key"; 
	private String PERSONS_REPO_KEY = "p_repo_key"; 
	
	private String PERSONS_CATEGORYKEY = "pers_cat_key";
	
	private final String PERSON_CFG = "test-data/facets-ctypes/persons-facets.xml";
	
	private final String POTLET_NAME = "personportlet";
	
	/** this XML file contains a list of persons for importing */
	private String ADMIN_IMPORT_PERSONS_XML = "test-data/facets-ctypes/persons-to-import.xml";
	
	private final String PERSONS_CATEGORY_NAME ="cat";
	
	private SiteService siteService = new SiteService();
	private ContentTypeService contentTypeService = new ContentTypeService();
	private RepositoryService repositoryService = new RepositoryService();
	private ContentService contentService = new ContentService();
	
	@Test(description = "setup: create preconditions. Create a site with section and portlet. Create a repository and category with image ctype, and publish content to the section")
	public void setup()
	{
		createPersonCType();
		createSite();
		allowSection();
		addSection();

		//create repository and category with ctype ='persons'
		createRepositoryAndCategory();
		
		importPersonContent();
		//add a portlet with a 'datasource' and term facet
		addPortlet();

	}
	
	@Test(description ="Create a term facet in a datasouce, check output. term name: 'top-3-lastname' ", dependsOnMethods = "setup")
	public void termsLastNameFacetTest()
	{
		  /*
        	<terms name="top-3-lastname">
            	<indexes>data.lastname</indexes>
            	<count>3</count>
            	<orderby>hits</orderby>
            </terms>  */
			logger.info("STARTED: #### Create a term facet in a datasouce(term name: 'top-3-lastname' ), check output ");
			Portlet portlet = (Portlet) getTestSession().get(PORTLET_FACET_KEY);
			//1.  press the button "preview data source".
			String pageSource = siteService.getPreviewDatasourceContent(getTestSession(), portlet);
			//3. verify expected and actual output
			//get terms from xml-datasource from web-page:
			List<Term> actualTerms = FacetTestUtils.getTermsFromUI(pageSource);
			int termCount =3;
			if(actualTerms.size() != termCount)
			{
				Assert.fail("Terms were not found in UI");
			}
			boolean result = true;
			for(Term term: actualTerms)
			{
				
				List<Person> personsFromFile = FacetTestUtils.getPersonsWithLasttname(ADMIN_IMPORT_PERSONS_XML, term.getValue());
				result &= personsFromFile.size() == term.getHits();
				if(personsFromFile.size() != term.getHits())
				{
					logger.info("actual and expected values are not equal. person with lastname:" + term.getValue() +"  hits from preview datasource is:: "+ term.getHits());
				}
				
			}
			Assert.assertTrue(result, "wrong value present in output ");
			logger.info(" FINISED $$$$$ Create a term facet in a datasouce, check output");
	}
	
	@Test(description ="Create a range facet in a datasouce, check output ", dependsOnMethods = "termsLastNameFacetTest")
	public void rangesFacetTest()
	{
			logger.info("STARTED ###  Create a range facet in a datasouce, check output");
			Portlet portlet = (Portlet) getTestSession().get(PORTLET_FACET_KEY);
			//1. edit portlet
			int categoryKey = (Integer) getTestSession().get(PERSONS_CATEGORYKEY);
			String datasource = FacetTestUtils.buildDataSourceString(categoryKey, DS_FACET_RANGES);
			portlet.setDatasource(datasource);
			siteService.editDatasourceTabInPortlet(getTestSession(), portlet);
			//2. press the button "preview data source".
			String pageSource = siteService.getPreviewDatasourceContent(getTestSession(), portlet);
			
			//get ranges from xml-datasource from web-page:
			List<Range> rangesActual = FacetTestUtils.getRangesFromPreview(pageSource);
			if(rangesActual.isEmpty())
			{
				Assert.fail("there are no ranges in preview datasource");
			}
			boolean result = true;
			for(Range range: rangesActual)
			{
				List<Person> personsFromFile = FacetTestUtils.getPersonsByRange(ADMIN_IMPORT_PERSONS_XML, range);
				result &= personsFromFile.size() == range.getHits();
			}
			//3. verify expected and actual output
			Assert.assertTrue(result, "wrong value present in output of 'data source preview'");
			logger.info(" FINISED $$$$$ Create a range facet in a datasouce, check output");
	}
	
	@Test(description ="Create a 'Histogram Facet '  in a datasouce, check output ", dependsOnMethods = "rangesFacetTest")
	public void histogramFacetTest()
	{
			logger.info(" STARTED #### Create a 'Histogram Facet '  in a datasouce, check output ");
			Portlet portlet = (Portlet) getTestSession().get(PORTLET_FACET_KEY);
			//1. edit portlet: set histogram facet
			int categoryKey = (Integer) getTestSession().get(PERSONS_CATEGORYKEY);
			String datasource = FacetTestUtils.buildDataSourceString(categoryKey, DS_FACET_HISTOGRAM);
			portlet.setDatasource(datasource);
			siteService.editDatasourceTabInPortlet(getTestSession(), portlet);
			//2. press the button "preview data source".
			String pageSource = siteService.getPreviewDatasourceContent(getTestSession(), portlet);
			
			//3. get histograms from xml-datasource from web-page:
			List<Histogram> histogramActual = FacetTestUtils.getHistogramFromPreview(pageSource);
			if(histogramActual.isEmpty())
			{
				Assert.fail("there are no histograms in preview datasource");
			}
			boolean result = true;
			for(Histogram histogram: histogramActual)
			{
				List<Person> personsFromFile = FacetTestUtils.getPersonsByBalance(ADMIN_IMPORT_PERSONS_XML, histogram.getValue());
				result &= personsFromFile.size() == histogram.getHits();
			}
			//3. verify expected and actual output
			Assert.assertTrue(result, "wrong value present in output of 'data source preview'");
			logger.info(" FINISED $$$$$ TEST PREVIEW  DATASOURCE::: histogram Facet");
	}

	@Test(description ="Create a 'DateHistogram Facet '  in a datasouce, check output ", dependsOnMethods = "histogramFacetTest")
	public void dateHistogramFacetTest() throws ParseException
	{
			logger.info(" STARTED #### Create a 'Histogram Facet '  in a datasouce, check output ");
			Portlet portlet = (Portlet) getTestSession().get(PORTLET_FACET_KEY);
			//1. edit portlet: set histogram facet
			int categoryKey = (Integer) getTestSession().get(PERSONS_CATEGORYKEY);
			String datasource = FacetTestUtils.buildDataSourceString(categoryKey, DS_FACET_DATEHISTOGRAM);
			portlet.setDatasource(datasource);
			siteService.editDatasourceTabInPortlet(getTestSession(), portlet);
			//2. press the button "preview data source".
			String pageSource = siteService.getPreviewDatasourceContent(getTestSession(), portlet);
			
			//3. get histograms from xml-datasource from web-page:
			List<DateHistogram> histogramActual = FacetTestUtils.getDateHistogramFromPreview(pageSource);
			boolean result = true;
			if(histogramActual.isEmpty())
			{
				Assert.fail("there are no datehistograms in preview datasource");
			}
			for(DateHistogram histogram: histogramActual)
			{
				List<Person> personsFromFile = FacetTestUtils.getPersonsByBirthYear(ADMIN_IMPORT_PERSONS_XML, histogram.getDate());
				result &= personsFromFile.size() == histogram.getHits();
			}
			//3. verify expected and actual output
			Assert.assertTrue(result, "wrong value present in output of 'data source preview'");
			logger.info(" FINISED $$$$$ TEST PREVIEW  DATASOURCE::: datehistogram Facet");
	}
	
	private void importPersonContent()
	{
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		// 1. import from an XML formatted resource
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-xml", ADMIN_IMPORT_PERSONS_XML, true, 30l, pathToCategory);
		table.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		logger.info("file: " + ADMIN_IMPORT_PERSONS_XML + "has imported");
		
		
	}

	/**
	 * create sample test-site.
	 */
	private void createSite()
	{
		logger.info("create new site and verify.");
		Site site = new Site();
		String siteName = "facetds" + Math.abs(new Random().nextInt());
		site.setDispalyName(siteName);
		site.setLanguage("English");
		SitesTableFrame table = siteService.createSite(getTestSession(), site);
		boolean result = table.verifyIsPresent(site.getDispalyName());
		Assert.assertTrue(result, "new site was not found in the table");
		getTestSession().put(SITE_FACET_KEY, site);
		logger.info("site created: " + siteName);
	}
	/**
	 * allows section page for site.
	 */
	private void allowSection()
	{
		Site site = (Site) getTestSession().get(SITE_FACET_KEY);
		logger.info("allow Section page type. site: " + site.getDispalyName());

		AllowedPageTypes[] allowedPageTypes = { AllowedPageTypes.SECTION };
		site.setAllowedPageTypes(allowedPageTypes);
		siteService.editSite(getTestSession(), site.getDispalyName(), site);
		logger.info("Section page type allowed. site: " + site.getDispalyName());

	}
	
	/**
	 * add section to the test-site
	 */
	private void addSection()
	{
		logger.info("#### STARTED: add new section menu item to the  Site ");
		Site site = (Site) getTestSession().get(SITE_FACET_KEY);
		SectionMenuItem section = new SectionMenuItem();
		section.setDisplayName("test");
		section.setShowInMenu(true);
		section.setMenuName("test");
		section.setOrdered(false);
		section.setSiteName(site.getDispalyName());
		List<String> list = new ArrayList<>();
		list.add(CNAME);
		section.setAvailableContentTypes(list);
		// 1. try to add a new section to Site:
		SiteMenuItemsTablePage siteItems = siteService.addSectionMenuItem(getTestSession(), site.getDispalyName(), section);
		// 2. verify: section present
		boolean result = siteItems.verifyIsPresent(section.getDisplayName());
		Assert.assertTrue(result, "section was not found in the table!");

		logger.info("section was added to site, site:" + site.getDispalyName());

	}

	
	/**
	 * creates 'Image' content type.
	 */ 
	private void createPersonCType()
	{
		logger.info("checks for the existance  of Content type, creates new content type if it does not exist");
		ContentType personType = new ContentType();
		personType.setName(CNAME);
		personType.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		personType.setDescription("content type for Facets test");
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(PERSON_CFG);
		String personCFG = TestUtils.getInstance().readConfiguration(in);
		personType.setConfiguration(personCFG);
		boolean isExist = contentTypeService.findContentType(getTestSession(), CNAME);
		if (!isExist)
		{
			contentTypeService.createContentType(getTestSession(), personType);
			logger.info("New content type with 'cusom content' handler was created");
		} else
		{
			logger.info("Image content already exists");
		}
	}
	
	/**
	 * create repository and  category with "Image" content type.
	 */
	private void createRepositoryAndCategory()
	{
		ContentRepository repository = new ContentRepository();
		repository.setName("testFacet" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);
		
		getTestSession().put(PERSONS_REPO_KEY, repository);
		String[] parents = { repository.getName() };
		ContentCategory personsCategory = ContentCategory.with().name(PERSONS_CATEGORY_NAME).contentTypeName(CNAME).parentNames(parents).build();
		
		repositoryService.addCategory(getTestSession(), personsCategory);
		getTestSession().put(IMPORT_CATEGORY_KEY, personsCategory);

		int catKey = repositoryService.getCategoryKey(getTestSession(), personsCategory.getName(), parents);
		getTestSession().put(PERSONS_CATEGORYKEY, Integer.valueOf(catKey));
		logger.info("category was created: cat-name" + personsCategory.getName());
	}
	

	/**
	 * adds sample portlet with 'getContentByCategory' datasource
	 */
	private void addPortlet()
	{
		Site site = (Site) getTestSession().get(SITE_FACET_KEY);
		Portlet portlet = new Portlet();
		portlet.setName(POTLET_NAME);
		STKResource stylesheet = new STKResource();
		stylesheet.setName("sample-module.xsl");
		stylesheet.setPath("modules", "module-sample-site");
		portlet.setStylesheet(stylesheet);
		portlet.setSiteName(site.getDispalyName());
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(DS_FACET_TERMS);
		String datasource = TestUtils.getInstance().readConfiguration(in);
		int index = datasource.indexOf("categoryKeys\">");
		StringBuffer sb = new StringBuffer(datasource);
		int key = (Integer) getTestSession().get(PERSONS_CATEGORYKEY);
		sb.insert(index + 14, key);

		portlet.setDatasource(sb.toString());
		SitePortletsTablePage table = siteService.addPortlet(getTestSession(), portlet);
		boolean result = table.verifyIsPresent(portlet.getName());
		Assert.assertTrue(result, "Portlet with name: " + portlet.getName() + " was not found in the table!");
		getTestSession().put(PORTLET_FACET_KEY, portlet);
	}



}
