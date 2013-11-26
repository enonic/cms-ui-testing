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
import com.enonic.autotests.model.userstores.BuiltInGroups;
import com.enonic.autotests.model.userstores.User;
import com.enonic.autotests.pages.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.pages.adminconsole.site.SiteMenuItemsTablePage;
import com.enonic.autotests.pages.adminconsole.site.SitePortletsTablePage;
import com.enonic.autotests.pages.adminconsole.site.SitesTableFrame;
import com.enonic.autotests.services.AccountService;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.services.RepositoryService;
import com.enonic.autotests.services.SiteService;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.utils.TestUtils;

public class TermStatsFacetTest extends BaseTest
{
	
private static final String DS_FACET_TERMSTATS = "test-data/facets-ctypes/terms-stats-balance-ds.xml";
	
	private static final String PORTLET_TSFACET_KEY = "portlet_tsfacet_key";
	private static final String IMPORT_CATEGORY_KEY = "import_category_key";
	
	private static final String ADMIN1_USER_KEY  = "admin1_user_key";
	private static final String ADMIN2_USER_KEY  = "admin2_user_key";
	
	private String CNAME = "facets-persons";
	private String SITE_TERMSTAT_FACET_KEY = "tsfacet_site_key"; 
	private String PERS_REPO_KEY = "pers_repo_key"; 
	
	private String PERSONS_CAT_KEY = "per_cat_key";
	private final String PASSWORD = "1q2w3e";
	private final String PERSONS_CATEGORY_NAME ="cat";
	
	private final String PERSON_CFG = "test-data/facets-ctypes/persons-facets.xml";
	
	private final String POTLET_NAME = "personportlet";
	
	/** this XML file contains a list of persons for importing */
	private String ADMIN_IMPORT_PERSONS_XML = "test-data/facets-ctypes/admin-import.xml";
	private String 	USER1_IMPORT = "test-data/facets-ctypes/user1-import.xml";
	private String 	USER2_IMPORT = "test-data/facets-ctypes/user2-import.xml";
	
	private SiteService siteService = new SiteService();
	private ContentTypeService contentTypeService = new ContentTypeService();
	private RepositoryService repositoryService = new RepositoryService();
	private ContentService contentService = new ContentService();
	private AccountService accountService = new AccountService();

	
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
	
	@Test(dependsOnMethods ="setup")
	public void addPersonForTermStats()
	{
		String name = "user" + Math.abs(new Random().nextInt());
		User user = User.with().name(name).password(PASSWORD).mail(name + "@mail.com").build();
		
		accountService.addUser(getTestSession(), user);
		List<String> groups = new ArrayList<>();
		groups.add(BuiltInGroups.ADMINISTRATORS.getValue());
		user.setGroups(groups);
		accountService.editUser(getTestSession(), user.getName(), user);
		getTestSession().put(ADMIN1_USER_KEY, user);
		
		name = "user" + Math.abs(new Random().nextInt());
	    user = User.with().name(name).password(PASSWORD).mail(name + "@mail.com").build();
		accountService.addUser(getTestSession(), user);
		user.setGroups(groups);
		accountService.editUser(getTestSession(), user.getName(), user);
		getTestSession().put(ADMIN2_USER_KEY, user);
	}
	
	@Test(description ="import additional persons",dependsOnMethods = "addPersonForTermStats")
	public void loginAndImport1()
	{
		User user = (User)getTestSession().get(ADMIN1_USER_KEY);
		getTestSession().setUser(user);
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		contentService.doImportContent(getTestSession(), "person-import-xml", USER1_IMPORT,true, AppConstants.PAGELOAD_TIMEOUT,  pathToCategory);
	}
	@Test(description ="import additional persons",dependsOnMethods = "loginAndImport1")
	public void loginAndImport2()
	{
		User user = (User)getTestSession().get(ADMIN2_USER_KEY);
		getTestSession().setUser(user);
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		contentService.doImportContent(getTestSession(), "person-import-xml", USER2_IMPORT,true, AppConstants.PAGELOAD_TIMEOUT,  pathToCategory);
	}
	
	
	@Test(description ="Create a facet 'terms-stats' in a datasouce, check output  ", dependsOnMethods = "loginAndImport2")
	public void termstatsFacetTest() throws ParseException
	{
			logger.info(" STARTED #### Create a facet 'terms-stats' in a datasouce, check output");
			Portlet portlet = (Portlet) getTestSession().get(PORTLET_TSFACET_KEY);
			
			//1. press the button "preview data source".
			String pageSource = siteService.getPreviewDatasourceContent(getTestSession(), portlet);
			
			//2. get terms from xml-datasource from web-page:
			List<Term> termstatsActual = FacetTestUtils.getTermsFromUI(pageSource);
			boolean result = true;
			// facet cfg:   <terms-stats name="top-3-persons-importer"> count ==3
			int termCount = 3;
			if(termstatsActual.size() !=termCount)
			{
				Assert.fail("wrong number of terms in the preview! size= "+termstatsActual.size());
			}
			User user1 = (User)getTestSession().get(ADMIN1_USER_KEY);
			User user2 = (User)getTestSession().get(ADMIN2_USER_KEY);
			result &= verifyTermStat(getTermForUser(termstatsActual, "admin"), ADMIN_IMPORT_PERSONS_XML, "admin");
			result &= verifyTermStat(getTermForUser(termstatsActual, user1.getName()), USER1_IMPORT, user1.getName());
			result &= verifyTermStat(getTermForUser(termstatsActual, user2.getName()), USER2_IMPORT, user2.getName());
			//3. verify expected and actual output
			Assert.assertTrue(result, "wrong value present in output of 'data source preview'");
			logger.info(" FINISED $$$$$ Create a facet 'terms-stats' in a datasouce, check output");
	}
	private Term getTermForUser(List<Term> termstatsActual, String userName)
	{
		for(Term term: termstatsActual)
		{
			if(term.getValue().contains(userName))
			{
				return term;
			}
		}
		return null;
	}
	private boolean verifyTermStat(Term term, String resFile, String userName)
	{
		boolean result = true;
		float total = FacetTestUtils.getTotalBalance(resFile);
		logger.info("total balance is " +total + " filename" + resFile + "username "+ userName);
		result &=FacetTestUtils.getPersons(resFile).size() == term.getHits();
		logger.info("hits is " +term.getHits());
		logger.info("result is " +result);
		result &= total == term.getSum();
		result &= term.getValue().contains(userName);
		return result;
	}
	
	private void importPersonContent()
	{
		ContentCategory categoryForImport = (ContentCategory) getTestSession().get(IMPORT_CATEGORY_KEY);
		String[] pathToCategory = new String[] { categoryForImport.getParentNames()[0], categoryForImport.getName() };
		// 1. import from an XML formatted resource
		ContentsTableFrame table = contentService.doImportContent(getTestSession(), "person-import-xml", ADMIN_IMPORT_PERSONS_XML,true, 30l, pathToCategory);
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
		String siteName = "termstats" + Math.abs(new Random().nextInt());
		site.setDispalyName(siteName);
		site.setLanguage("English");
		SitesTableFrame table = siteService.createSite(getTestSession(), site);
		boolean result = table.verifyIsPresent(site.getDispalyName());
		Assert.assertTrue(result, "new site was not found in the table");
		getTestSession().put(SITE_TERMSTAT_FACET_KEY, site);
		logger.info("site created: " + siteName);
	}
	/**
	 * allows section page for site.
	 */
	private void allowSection()
	{
		Site site = (Site) getTestSession().get(SITE_TERMSTAT_FACET_KEY);
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
		logger.info("#### STARTED: add new  section menu item to the  Site ");
		Site site = (Site) getTestSession().get(SITE_TERMSTAT_FACET_KEY);
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
		repository.setName("termStats" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);
		
		getTestSession().put(PERS_REPO_KEY, repository);
		String[] parents = { repository.getName() };
		ContentCategory personsCategory = ContentCategory.with().name(PERSONS_CATEGORY_NAME).contentTypeName(CNAME).parentNames(parents).build();
		
		repositoryService.addCategory(getTestSession(), personsCategory);
		getTestSession().put(IMPORT_CATEGORY_KEY, personsCategory);

		int catKey = repositoryService.getCategoryKey(getTestSession(), personsCategory.getName(), parents);
		getTestSession().put(PERSONS_CAT_KEY, Integer.valueOf(catKey));
		logger.info("category was created: cat-name" + personsCategory.getName());
	}
	

	/**
	 * adds sample portlet with 'getContentByCategory' datasource
	 */
	private void addPortlet()
	{
		Site site = (Site) getTestSession().get(SITE_TERMSTAT_FACET_KEY);
		Portlet portlet = new Portlet();
		portlet.setName(POTLET_NAME);
		STKResource stylesheet = new STKResource();
		stylesheet.setName("sample-module.xsl");
		stylesheet.setPath("modules", "module-sample-site");
		portlet.setStylesheet(stylesheet);
		portlet.setSiteName(site.getDispalyName());
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(DS_FACET_TERMSTATS);
		String datasource = TestUtils.getInstance().readConfiguration(in);
		int index = datasource.indexOf("categoryKeys\">");
		StringBuffer sb = new StringBuffer(datasource);
		int key = (Integer) getTestSession().get(PERSONS_CAT_KEY);
		sb.insert(index + 14, key);

		portlet.setDatasource(sb.toString());
		SitePortletsTablePage table = siteService.addPortlet(getTestSession(), portlet);
		boolean result = table.verifyIsPresent(portlet.getName());
		Assert.assertTrue(result, "Portlet with name: " + portlet.getName() + " was not found in the table!");
		getTestSession().put(PORTLET_TSFACET_KEY, portlet);
	}



	
}
