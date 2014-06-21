/****************************************************************

  Traction Software, Inc. Confidential and Proprietary Information

  Copyright (c) 1996-2014 Traction Software, Inc.
  All rights reserved.

****************************************************************/

// PLEASE DO NOT DELETE THIS LINE -- make copyright depends on it.

package com.tractionsoftware.gwt.user.client.ui;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * Tests for the GroupedListBox
 *
 * @author andy
 */
public class GwtTestGroupedListBox extends GWTTestCase {

    private GroupedListBox box;

    @Override
    public void gwtSetUp() {
        box = new GroupedListBox(false);
    }

    @Override
    public String getModuleName() {
        return "com.tractionsoftware.gwt.user.GroupedListBox";
    }

    private void verifyItem(int i, String name, String value) {
        assertEquals(name, box.getItemText(i));
        assertEquals(value, box.getValue(i));
    }

    @SuppressWarnings("unused")
    private void verifyItem(int i, String name, String value, boolean selected) {
        verifyItem(i, name, value);
	assertEquals(selected, box.isItemSelected(i));
    }

    private void verifyIndexOutOfBounds(int i) {
        try {
            box.getItemText(i);
            fail("Expected IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException e) {}

        try {
            box.getValue(i);
            fail("Expected IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException e) {}

        try {
            box.isItemSelected(i);
            fail("Expected IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException e) {}
    }

    public void testGroupsExtracted() {
        assertEquals(0, box.getIndexOfFirstGroup());

        box.addItem("Group A|Item A1", "A1");
        assertEquals(0, box.getIndexOfFirstGroup());

        box.addItem("Group A|Item A2", "A2");
        assertEquals(0, box.getIndexOfFirstGroup());

        box.addItem("Group B|Item B1", "B1");
        assertEquals(0, box.getIndexOfFirstGroup());

        box.addItem("Group B|Item B2", "B2");
        assertEquals(0, box.getIndexOfFirstGroup());

        assertEquals(4, box.getItemCount());
        verifyIndexOutOfBounds(-1);
        verifyItem(0, "Item A1", "A1");
        verifyItem(1, "Item A2", "A2");
        verifyItem(2, "Item B1", "B1");
        verifyItem(3, "Item B2", "B2");
        verifyIndexOutOfBounds(4);

        assertEquals(box.getItemCount(), box.getItemCountFromGroups());
    }


    public void testGroupsUnordered() {
        box.addItem("Group A|Item A1", "A1");
        box.addItem("Group B|Item B1", "B1");
        box.addItem("Group A|Item A2", "A2");
        box.addItem("Group B|Item B2", "B2");

        verifyItem(0, "Item A1", "A1");
        verifyItem(1, "Item A2", "A2");
        verifyItem(2, "Item B1", "B1");
        verifyItem(3, "Item B2", "B2");

        assertEquals(box.getItemCount(), box.getItemCountFromGroups());
    }


    public void testSomeUngrouped() {
        box.addItem("Item 10", "10");
        box.addItem("Group A|Item A1", "A1");
        box.addItem("Group B|Item B1", "B1");
        box.addItem("Group A|Item A2", "A2");
        box.addItem("Group B|Item B2", "B2");

        assertEquals(5, box.getItemCount());
        verifyItem(0, "Item 10", "10");
        verifyItem(1, "Item A1", "A1");
        verifyItem(2, "Item A2", "A2");
        verifyItem(3, "Item B1", "B1");
        verifyItem(4, "Item B2", "B2");

        assertEquals(box.getItemCount(), box.getItemCountFromGroups());

        box.addItem("Item 20", "20");

        assertEquals(box.getItemCount(), box.getItemCountFromGroups());
        assertEquals(6, box.getItemCount());
        verifyItem(0, "Item 10", "10");
        verifyItem(1, "Item 20", "20");
        verifyItem(2, "Item A1", "A1");
        verifyItem(3, "Item A2", "A2");
        verifyItem(4, "Item B1", "B1");
        verifyItem(5, "Item B2", "B2");

        assertEquals(box.getItemCount(), box.getItemCountFromGroups());
    }


    public void testEscapedGrouped() {
        box.addItem("Item 10", "10");
        box.addItem("Group || A|Item A1", "A1");
        box.addItem("Group B|Item B1", "B1");
        box.addItem("Group || A|Item A2", "A2");
        box.addItem("Group B|Item B2", "B2");
        box.addItem("Item 20", "20");

        assertEquals(6, box.getItemCount());
        verifyItem(0, "Item 10", "10");
        verifyItem(1, "Item 20", "20");
        verifyItem(2, "Item A1", "A1");
        verifyItem(3, "Item A2", "A2");
        verifyItem(4, "Item B1", "B1");
        verifyItem(5, "Item B2", "B2");

        assertEquals(box.getItemCount(), box.getItemCountFromGroups());
    }

    public void testSelectedIndex() {
        box.addItem("Group A|Item A1", "A1");
        box.addItem("Group B|Item B1", "B1");
        box.addItem("Group A|Item A2", "A2");
        box.addItem("Group B|Item B2", "B2");

        box.setSelectedIndex(2);
        assertFalse(box.isItemSelected(0));
        assertFalse(box.isItemSelected(1));
        assertTrue(box.isItemSelected(2));
        assertFalse(box.isItemSelected(3));

        box.setSelectedIndex(1);
        assertFalse(box.isItemSelected(0));
        assertTrue(box.isItemSelected(1));
        assertFalse(box.isItemSelected(2));
        assertFalse(box.isItemSelected(3));

        box.setItemSelected(3, true);
        assertFalse(box.isItemSelected(0));
        assertFalse(box.isItemSelected(1));
        assertFalse(box.isItemSelected(2));
        assertTrue(box.isItemSelected(3));

        assertEquals(box.getItemCount(), box.getItemCountFromGroups());
    }

    public void testSetItemText() {
        box.addItem("Group A|Item A1", "A1");
        box.addItem("Group B|Item B1", "B1");
        box.addItem("Group A|Item A2", "A2");
        box.addItem("Group B|Item B2", "B2");

        box.setItemText(1, "Item A2.1");
        verifyItem(0, "Item A1", "A1");
        verifyItem(1, "Item A2.1", "A2");
        verifyItem(2, "Item B1", "B1");
        verifyItem(3, "Item B2", "B2");

        box.setItemText(2, "Item B1.1");
        verifyItem(0, "Item A1", "A1");
        verifyItem(1, "Item A2.1", "A2");
        verifyItem(2, "Item B1.1", "B1");
        verifyItem(3, "Item B2", "B2");

        assertEquals(box.getItemCount(), box.getItemCountFromGroups());
    }

    // we don't parse setItemText
    public void testSetItemTextWithGroup() {
        box.addItem("Group A|Item A1", "A1");
        box.addItem("Group B|Item B1", "B1");
        box.addItem("Group A|Item A2", "A2");
        box.addItem("Group B|Item B2", "B2");

        box.setItemText(1, "Group B|Item A2");
        verifyItem(0, "Item A1", "A1");
        verifyItem(1, "Group B|Item A2", "A2");
        verifyItem(2, "Item B1", "B1");
        verifyItem(3, "Item B2", "B2");
    }

    public void testSetValue() {
        box.addItem("Group A|Item A1", "A1");
        box.addItem("Group B|Item B1", "B1");
        box.addItem("Group A|Item A2", "A2");
        box.addItem("Group B|Item B2", "B2");

        box.setValue(1, "A2.1");
        verifyItem(0, "Item A1", "A1");
        verifyItem(1, "Item A2", "A2.1");
        verifyItem(2, "Item B1", "B1");
        verifyItem(3, "Item B2", "B2");

        box.setValue(2, "B1.1");
        verifyItem(0, "Item A1", "A1");
        verifyItem(1, "Item A2", "A2.1");
        verifyItem(2, "Item B1", "B1.1");
        verifyItem(3, "Item B2", "B2");
    }

    public void testInsertGroupAdjusted() {
        box.addItem("Group A|Item A1", "A1");
        box.addItem("Group B|Item B1", "B1");
        box.addItem("Group A|Item A2", "A2");
        box.addItem("Group B|Item B2", "B2");

        box.insertItem("Group B|Item B0", "B0", 0);
        verifyItem(0, "Item A1", "A1");
        verifyItem(1, "Item A2", "A2");
        verifyItem(2, "Item B0", "B0");
        verifyItem(3, "Item B1", "B1");
        verifyItem(4, "Item B2", "B2");

        assertEquals(box.getItemCount(), box.getItemCountFromGroups());
    }

    public void testAddNull() {
        try {
            box.addItem(null);
            box.addItem((String)null, (String)null);
        }
        catch (NullPointerException e) {
            fail("NullPointerException should not be thrown for null item");
        }
    }

    public void testClear() {
        box.addItem("Group A|Item A1", "A1");
        box.addItem("Group B|Item B1", "B1");
        box.addItem("Group A|Item A2", "A2");
        box.addItem("Group B|Item B2", "B2");
        box.clear();

        assertEquals(0, box.getItemCount());
        assertEquals(0, box.getElement().getChildCount());
    }

    public void testClear2() {
        box.addItem("Item 1", "1");
        box.addItem("Group A|Item A2", "A2");
        box.addItem("Group B|Item B1", "B1");
        box.addItem("Group B|Item B2", "B2");
        box.clear();

        assertEquals(0, box.getItemCount());
        assertEquals(0, box.getElement().getChildCount());
    }

    public void testAddsAndRemoves() {
        box.addItem("Group A|Item A1", "A1");
        box.addItem("Group A|Item A2", "A2");
        box.addItem("Group B|Item B1", "B1");
        box.addItem("Group B|Item B2", "B2");

        box.removeItem(2);
        verifyItem(0, "Item A1", "A1");
        verifyItem(1, "Item A2", "A2");
        verifyItem(2, "Item B2", "B2");
        assertEquals(3, box.getItemCount());
        assertEquals(2, box.getElement().getChildCount());

        box.removeItem(2);
        verifyItem(0, "Item A1", "A1");
        verifyItem(1, "Item A2", "A2");
        assertEquals(2, box.getItemCount());
        assertEquals(1, box.getElement().getChildCount());

        box.removeItem(0);
        box.removeItem(0);
        assertEquals(0, box.getItemCount());
        assertEquals(0, box.getElement().getChildCount());

        box.addItem("Group A|Item A1", "A1");
        assertEquals(1, box.getItemCount());
        assertEquals(1, box.getElement().getChildCount());

        box.addItem("Group B|Item B1", "B1");
        assertEquals(2, box.getItemCount());
        assertEquals(2, box.getElement().getChildCount());
    }

    public void testInsertMany() {
        long start = System.currentTimeMillis();
        box.addItem("","");
        box.addItem("Academia NI Mihai Viteazul|Install and test Traction Team...","1730871820294");
        box.addItem("AKJ|Attivio Support","442381644060");
        box.addItem("AKJ|Issues, Bugs and Features","442381644137");
        box.addItem("AKJ|Outlook Social Connector","442381644069");
        box.addItem("AKJ|TeamPage Support","442381644577");
        box.addItem("AKJ|Traction Instant Publisher","442381644067");
        box.addItem("Alcoa|Issues and Bug Reports","1206885811077");
        box.addItem("Alcoa|Project For Testing In the Alc...","1206885810850");
        box.addItem("Alcoa|Technical Support","1206885811076");
        box.addItem("Allstate|Technical Support","1065151889571");
        box.addItem("Amazon Web Services|Cost Savings","1125281431713");
        box.addItem("Amazon Web Services|Hosted Server Maintenance","1125281431705");
        box.addItem("Amazon Web Services|Infrastructure Improvement","1125281431707");
        box.addItem("Andy|gwt-traction","25769804462");
        box.addItem("Armstrong|TeamPage Development","1408749273182");
        box.addItem("Armstrong|Translations","1408749273221");
        box.addItem("Athens Group|Issues &amp; Bug Reports","1348619731253");
        box.addItem("Athens Group|Technical Support","1348619731249");
        box.addItem("Attivio|Active Search","1301375092064");
        box.addItem("Attivio|GE Healthcare RFP","1301375091575");
        box.addItem("Attivio|Integrate Attivio 2.2","1301375091128");
        box.addItem("Attivio|Japanese Integration","1301375091097");
        box.addItem("Avery Dennison|Technical Support","1159641170869");
        box.addItem("Bain|Technical Support","1116691497514");
        box.addItem("Baldor|Technical Support","1700807049222");
        box.addItem("Boston Consulting Group|NTLMv2 Auth on BCG test server","712964575465");
        box.addItem("Boston Consulting Group|Technical Support","712964575599");
        box.addItem("Boston Scientific|Technical Support","1284195221545");
        box.addItem("CBC|Technical Support","1292785156415");
        box.addItem("Client|Mac TIP","120259088040");
        box.addItem("Client|Other Desktop Tools","120259088444");
        box.addItem("Client|Outlook Social Connector","120259088038");
        box.addItem("Client|TIP 3","120259088036");
        box.addItem("Client|TIP 3 Support","120259088293");
        box.addItem("Client|Toolbar Buttons","120259088542");
        box.addItem("Copper Dev|Technical Support","1752346656776");
        box.addItem("DB|Feature-Complete Supportable R...","1129576402642");
        box.addItem("DB|Optimizations and Other Improv...","1129576402672");
        box.addItem("Debevoise &amp; Plimpton|Technical Support","1279900254621");
        box.addItem("Decagon|Technical Support","1318554960082");
        box.addItem("Demo|Another Project","47244642494");
        box.addItem("Documentation Feedback|Needs Updating","1275605287330");
        box.addItem("Documentation Feedback|Proteus-Specific Help","1275605287329");
        box.addItem("Dyncorp|Technical Support","1537598292003");
        box.addItem("Engin|Capture Metrics","253403072768");
        box.addItem("Engin|Improve Development/Build Envi...","253403072787");
        box.addItem("Engin|Moving java source into src/ f...","253403072809");
        box.addItem("Engin|XEmacs to Aquamacs","253403072992");
        box.addItem("Ensign-Bickford|Development and Customization","1340029796699");
        box.addItem("Ensign-Bickford|Technical Support","1340029796698");
        box.addItem("Ensign-Bickford|Test PM","1340029796693");
        box.addItem("Eskilstuna Data Mekan|Internavvikelse Form","1821066133509");
        box.addItem("EuroCopper|Technical Support","1571958030467");
        box.addItem("FDA|Development and Customization","1189705942816");
        box.addItem("FDA|Technical Support","1189705942824");
        box.addItem("FINRA|Feedback (Issues &amp; Bug Reports)","1709396983813");
        box.addItem("FINRA|Technical Support","1709396983811");
        box.addItem("Forum|2012 Master Planning Process","953482744463");
        box.addItem("Forum|T Projekt","953482744643");
        box.addItem("Forum|Разработка интерфейса","953482744814");
        box.addItem("Help|Technical Support","352187326338");
        box.addItem("IntegralDevCorp|Technical Support","1769526525957");
        box.addItem("Internal|Discussion for Andy's Visit","270582947631");
        box.addItem("Internal|Grow Business","270582947323");
        box.addItem("Internal|Take down Knowesys Server?","270582947806");
        box.addItem("Internal|Track Metrics and Conversion","270582947415");
        box.addItem("Internal|Update Public Web Site","270582947327");
        box.addItem("Ipsen|Issues and Bug Reports","502511178183");
        box.addItem("Ipsen|Migrate CI to Proteus","502511177723");
        box.addItem("Ipsen|MIND","502511177372");
        box.addItem("Ipsen|MIND Reporting","502511178216");
        box.addItem("Ipsen|Technical Support","502511177085");
        box.addItem("Los Alamos National Labs|Technical Support","1520418422981");
        box.addItem("Los Alamos National Labs|Test for Project","1520418422851");
        box.addItem("Marketing|Demonstrate TeamPage Project M...","313532625874");
        box.addItem("Marketing|E2.0 East 2012 Boston - June","313532626141");
        box.addItem("Marketing|E2.0 West 2011 - Santa Clara -...","313532625683");
        box.addItem("MCorp|Technical Support","1468878815410");
        box.addItem("New Shoreham Consulting|Technical Support","1825361100802");
        box.addItem("NHS UK|Technical Support","1095216661477");
        box.addItem("NSWCCD|Technical Support","1413044240447");
        box.addItem("Occom Group|Resource Pro Test project","1657857376318");
        box.addItem("Proteus|Activity Feed","1443109019681");
        box.addItem("Proteus|Calendaring","1443109014842");
        box.addItem("Proteus|Cross-Space Action Tracking","1443109017495");
        box.addItem("Proteus|Custom Stylesheets","1443109019453");
        box.addItem("Proteus|Customizable Tabs","1443109015776");
        box.addItem("Proteus|Data Export","1443109016576");
        box.addItem("Proteus|Discussion Widget","1443109019823");
        box.addItem("Proteus|Documents","1443109017862");
        box.addItem("Proteus|Editor &gt; Project","1443109015158");
        box.addItem("Proteus|Editor &gt; Task","1443109015074");
        box.addItem("Proteus|Email Notification","1443109015380");
        box.addItem("Proteus|Extensible GWT SDL Forms","1443109016571");
        box.addItem("Proteus|File Drag/Drop","1443109019137");
        box.addItem("Proteus|General Issues and Refinements","1443109015084");
        box.addItem("Proteus|Inline Tasks","1443109016479");
        box.addItem("Proteus|Mobile Device Support","1443109016161");
        box.addItem("Proteus|Multi-entry reclassify dialog","1443109017735");
        box.addItem("Proteus|Page Updating","1443109019432");
        box.addItem("Proteus|Profile &gt; Editor","1443109019383");
        box.addItem("Proteus|Profile &gt; Projects","1443109015175");
        box.addItem("Proteus|Profile &gt; Tasks","1443109015171");
        box.addItem("Proteus|Project Management Suite","1443109014853");
        box.addItem("Proteus|Proteus Makeover","1443109019004");
        box.addItem("Proteus|Proteus SDK","1443109015940");
        box.addItem("Proteus|Roadmap &gt; Admin","1443109017973");
        box.addItem("Proteus|Roadmap &gt; Documents","1443109017983");
        box.addItem("Proteus|Roadmap &gt; Extranet","1443109017963");
        box.addItem("Proteus|Roadmap &gt; Help","1443109017930");
        box.addItem("Proteus|Roadmap &gt; Hosting","1443109018084");
        box.addItem("Proteus|Roadmap &gt; Microblogging","1443109017886");
        box.addItem("Proteus|Roadmap &gt; Notification","1443109017936");
        box.addItem("Proteus|Roadmap &gt; PM","1443109017914");
        box.addItem("Proteus|Roadmap &gt; Q+A, Ideas, and Ratings","1443109017985");
        box.addItem("Proteus|Roadmap &gt; Search","1443109018077");
        box.addItem("Proteus|Roadmap &gt; Social Bookmarking","1443109018033");
        box.addItem("Proteus|Roadmap &gt; TP+1","1443109017991");
        box.addItem("Proteus|Roadmap &gt; Viral Adoption","1443109017946");
        box.addItem("Proteus|Sections","1443109019488");
        box.addItem("Proteus|Single Entry &gt; Milestone","1443109015080");
        box.addItem("Proteus|Single Entry &gt; Project","1443109015143");
        box.addItem("Proteus|Single Entry &gt; Task","1443109015056");
        box.addItem("Proteus|SQL PM Implementation","1443109015182");
        box.addItem("Proteus|Static Resource Cleanup and St...","1443109019283");
        box.addItem("Proteus|Status Updates","1443109015894");
        box.addItem("Proteus|Task List","1443109015116");
        box.addItem("Proteus|Tasks &gt; Milestones Tab","1443109015308");
        box.addItem("Proteus|Tasks &gt; Projects Tab","1443109015102");
        box.addItem("Proteus|Widgets","1443109019527");
        box.addItem("QICSupport|Technical Support","777389084726");
        box.addItem("Quality Chain Canada|Technical Support","1735166787586");
        box.addItem("Roche|Co-Occurrence of Terms Report","962072676059");
        box.addItem("Roche|Technical Support","962072676015");
        box.addItem("Sales|Quote Work","210453408170");
        box.addItem("Sandbox|john tropeas test","996432414302");
        box.addItem("Sandbox|My project","996432414391");
        box.addItem("Sandbox|New project","996432413990");
        box.addItem("Sandbox|Project #1","996432414259");
        box.addItem("Sandbox|Project One","996432414032");
        box.addItem("Sandbox|Project Three","996432414036");
        box.addItem("Sandbox|Project Two","996432414034");
        box.addItem("Sandbox|Sample Project #1","996432414395");
        box.addItem("Sandbox|Sample Project #2","996432414396");
        box.addItem("Sandbox|Test","996432414335");
        box.addItem("Sandbox|Testmitwiki","996432414085");
        box.addItem("Sandbox|testprojectr","996432414064");
        box.addItem("Sanofi|Technical Support","970662609462");
        box.addItem("Server|AIE Integration","150323910463");
        box.addItem("Server|Attivio+ Integration and Packa...","150323910968");
        box.addItem("Server|Authentication","150323910462");
        box.addItem("Server|Configuration and Plug-ins","150323910456");
        box.addItem("Server|Editor &gt; General","150323910477");
        box.addItem("Server|Email Articles","150323910469");
        box.addItem("Server|Email Digest","150323909236");
        box.addItem("Server|Email Ingestion","150323910454");
        box.addItem("Server|Emergency Maintenance for Hist...","150323907076");
        box.addItem("Server|Event Listeners / Daemons","150323915120");
        box.addItem("Server|Export Articles","150323910484");
        box.addItem("Server|Feed Reader","150323910506");
        box.addItem("Server|i18n and l10n","150323910538");
        box.addItem("Server|Installer","150323909462");
        box.addItem("Server|Journal / Metadata / Native Fu...","150323910458");
        box.addItem("Server|LDAP and Active Directory","150323910451");
        box.addItem("Server|Live Activity","150323912102");
        box.addItem("Server|Metrics and Logging","150323907108");
        box.addItem("Server|Move and refresh TeamPage videos","150323914139");
        box.addItem("Server|Optimizations and Performance","150323915307");
        box.addItem("Server|Permissions Model","150323912044");
        box.addItem("Server|Print Version","150323913203");
        box.addItem("Server|Production Server Customizations","150323908197");
        box.addItem("Server|Public Website Maintenance","150323907692");
        box.addItem("Server|Rapid Selector","150323910470");
        box.addItem("Server|SDK &gt; Entry Query Implementation","150323912104");
        box.addItem("Server|SDK and SDL","150323910452");
        box.addItem("Server|Setup and Administration","150323910465");
        box.addItem("Server|Social Bookmarking","150323910842");
        box.addItem("Server|Startup / Shutdown","150323910457");
        box.addItem("Server|Subscriptions and Notifications","150323910461");
        box.addItem("Server|Support Tools","150323907766");
        box.addItem("Server|Syndication Feeds","150323910486");
        box.addItem("Server|Test Suite","150323908172");
        box.addItem("Server|Token Rendering","150323913584");
        box.addItem("Server|Tokenization and Parsing","150323910464");
        box.addItem("Server|Unclassified Issues and Improv...","150323910448");
        box.addItem("Server|Web Server","150323910472");
        box.addItem("Server|WebDAV Backend","150323910466");
        box.addItem("Shep|Slides/Docs for TUG Training","468151436763");
        box.addItem("SPO|Goals for New Website","446676603773");
        box.addItem("SPO|Identify Problems with Current...","446676603771");
        box.addItem("SPO|Metrics to Gather/Reports to M...","446676603800");
        box.addItem("Sunflower Bank|Technical Support","1687922147381");
        box.addItem("T-Mobile|Feature Requests and Suggestions","1194000909117");
        box.addItem("T-Mobile|Issues and Bug Reports","1194000908926");
        box.addItem("T-Mobile|Technical Support","1194000908922");
        box.addItem("Tega Industries|Issues and Bug Reports","1636382540054");
        box.addItem("Tega Industries|Technical Support","1636382540052");
        box.addItem("TUG Planning|Produce TUG 2010 Videos","1387274437254");
        box.addItem("UMB|Development and Customization","1378684502743");
        box.addItem("UMB|Technical Support","1378684502741");
        box.addItem("Univ Cal Riverside|Dashboard Modifications for Vi...","1756641624094");
        box.addItem("Univ Cal Riverside|Digest Modifications for UCal ...","1756641624123");
        box.addItem("Univ Cal Riverside|Technical Support","1756641624073");
        box.addItem("Wellington|Technical Support","1172526072244");
        box.addItem("Wheat Rust Research|Technical Support","1241245549413");

        assertEquals(206, box.getItemCount());
        assertEquals(60, box.getElement().getChildCount());
        assertEquals(box.getItemCount(), box.getItemCountFromGroups());

        long end = System.currentTimeMillis();
        System.err.println("testInsertMany took "+(end-start)/1000.0+" ms");
    }

}
