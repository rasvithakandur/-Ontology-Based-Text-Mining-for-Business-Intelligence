import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    static String xaxis;
    static String yaxis;
    static LabeledXYDataset ds = new LabeledXYDataset();

    private static class LabeledXYDataset extends AbstractXYDataset {

        private static final int N = 26;
        private List<Number> x = new ArrayList<Number>(N);
        private List<Number> y = new ArrayList<Number>(N);
        private List<String> label = new ArrayList<String>(N);

        public void add(double x, double y, String label) {
            this.x.add(x);
            this.y.add(y);
            this.label.add(label);
        }

        public String getLabel(int series, int item) {
            return label.get(item);
        }

        @Override
        public int getSeriesCount() {
            return 1;
        }

        @Override
        public Comparable getSeriesKey(int series) {
            return "Unit";
        }

        @Override
        public int getItemCount(int series) {
            return label.size();
        }

        @Override
        public Number getX(int series, int item) {
            return x.get(item);
        }

        @Override
        public Number getY(int series, int item) {
            return y.get(item);
        }
    }

    private static class LabelGenerator implements XYItemLabelGenerator {

        @Override
        public String generateLabel(XYDataset dataset, int series, int item) {
            LabeledXYDataset labelSource = (LabeledXYDataset) dataset;
            return labelSource.getLabel(series, item);
        }

    }


    public static void main(String[] args) {
        System.out.println("Hello World!");
        gui();
    }

    static void gui() {

        // create JFrame and JTable
        JFrame frame = new JFrame();
        JTable table = new JTable();

        // create a table model and set a Column Identifiers to this model
        Object[] columns = {"A", "B", "C"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);

        // set the model to the table
        table.setModel(model);

        //label
        JLabel queryLabel = new JLabel();
        queryLabel.setText("Enter Query");
        queryLabel.setBounds(950, 10, 100, 25);

        //query field
        JTextField query = new JTextField();
        query.setBounds(1050, 10, 120, 25);

        //radio buttons
        JCheckBox life = new JCheckBox("Life");
        life.setBounds(10, 10, 100, 25);
        JCheckBox happiness = new JCheckBox("Happiness");
        happiness.setBounds(100, 10, 100, 25);
        JCheckBox gdp = new JCheckBox("GDP");
        gdp.setBounds(200, 10, 100, 25);
        JCheckBox pollution = new JCheckBox("Pollution");
        pollution.setBounds(300, 10, 100, 25);
        JCheckBox immigration = new JCheckBox("Immigration");
        immigration.setBounds(400, 10, 120, 25);
        JCheckBox population = new JCheckBox("Population");
        population.setBounds(520, 10, 100, 25);


        //search button
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(680, 10, 100, 25);

        JButton clearButton = new JButton("Plot");
        clearButton.setBounds(800, 10, 100, 25);


        // create JScrollPane
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(20, 40, 1235, 650);

        frame.setLayout(null);

        // add JTextFields to the jframe
        frame.add(queryLabel);
        frame.add(query);
//

        frame.add(searchButton);
        frame.add(gdp);
        frame.add(happiness);
        frame.add(life);
        frame.add(pollution);
        frame.add(population);
        frame.add(immigration);
        frame.add(clearButton);
        frame.add(queryLabel);
        frame.add(query);

        //add table
        frame.add(pane);

        // button add row
        searchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String selection = "";
                if (life.isSelected()) selection += "life";
                if (happiness.isSelected()) selection += "happiness";
                if (gdp.isSelected()) selection += "gdp";
                if (pollution.isSelected()) selection += "pollution";
                if (immigration.isSelected()) selection += "immigration";
                if (population.isSelected()) selection += "population";
                selection += query.getText();
                ds = new LabeledXYDataset();

                switch (selection) {
                    case "happinesspollution":
                        Object[] columns2 = {"Country Name", "Happiness", "Pollution"};
                        List<Cell> list2 = happinessPollution();
                        model.setColumnIdentifiers(columns2);
                        model.setRowCount(0);
                        xaxis = "Happiness";
                        yaxis = "Pollution";
                        for (Cell data : list2) {
                            model.addRow(new Object[]{data.a, data.b, data.c});
                            ds.add(Double.parseDouble(data.b), Double.parseDouble(data.c), data.a.substring(0, Math.min(data.a.length(), 3)));
                        }
                        break;
                    case "happinesspopulation":
                        Object[] columns3 = {"Country Name", "Happiness", "Population"};
                        List<Cell> list3 = happinessPopulation();
                        model.setColumnIdentifiers(columns3);
                        model.setRowCount(0);
                        xaxis = "Happiness";
                        yaxis = "Population";
                        for (Cell data : list3) {
                            model.addRow(new Object[]{data.a, data.b, data.c});
                            ds.add(Double.parseDouble(data.b), Double.parseDouble(data.c), data.a.substring(0, Math.min(data.a.length(), 3)));
                        }
                        break;
                    case "happinessgdp":
                        Object[] columns4 = {"Country Name", "Happiness", "GDP"};
                        List<Cell> list4 = happinessGdp();
                        model.setColumnIdentifiers(columns4);
                        model.setRowCount(0);
                        xaxis = "Happiness";
                        yaxis = "GDP";
                        for (Cell data : list4) {
                            model.addRow(new Object[]{data.a, data.b, data.c});
                            ds.add(Double.parseDouble(data.b), Double.parseDouble(data.c), data.a.substring(0, Math.min(data.a.length(), 3)));
                        }
                        break;
                    case "happinessimmigration":
                        Object[] columns5 = {"Country Name", "Happiness", "Immigration"};
                        List<Cell> list5 = happinessImmigration();
                        model.setColumnIdentifiers(columns5);
                        model.setRowCount(0);
                        xaxis = "Happiness";
                        yaxis = "Immigration";
                        for (Cell data : list5) {
                            model.addRow(new Object[]{data.a, data.b, data.c});
                            ds.add(Double.parseDouble(data.b), Double.parseDouble(data.c), data.a.substring(0, Math.min(data.a.length(), 3)));
                        }
                        break;
                    case "lifehappiness":
                        Object[] columns6 = {"Country Name", "Happiness", "life"};
                        List<Cell> list6 = happinessLife();
                        model.setColumnIdentifiers(columns6);
                        model.setRowCount(0);
                        xaxis = "Happiness";
                        yaxis = "Life";
                        for (Cell data : list6) {
                            model.addRow(new Object[]{data.a, data.b, data.c});
                            ds.add(Double.parseDouble(data.b), Double.parseDouble(data.c), data.a.substring(0, Math.min(data.a.length(), 3)));
                        }
                        break;
                    case "lifegdp":
                        Object[] columns7 = {"Country Name", "Life Expectancy", "GDP"};
                        List<Cell> list7 = lifeGdp();
                        model.setColumnIdentifiers(columns7);
                        model.setRowCount(0);
                        xaxis = "Life";
                        yaxis = "GDP";
                        for (Cell data : list7) {
                            model.addRow(new Object[]{data.a, data.b, data.c});
                            ds.add(Double.parseDouble(data.b), Double.parseDouble(data.c), data.a.substring(0, Math.min(data.a.length(), 3)));
                        }
                        break;
                    case "lifepopulation":
                        Object[] columns8 = {"Country Name", "Life", "Population"};
                        List<Cell> list8 = lifePopulation();
                        model.setColumnIdentifiers(columns8);
                        model.setRowCount(0);
                        xaxis = "Life";
                        yaxis = "Population";
                        for (Cell data : list8) {
                            model.addRow(new Object[]{data.a, data.b, data.c});
                            ds.add(Double.parseDouble(data.b), Double.parseDouble(data.c), data.a.substring(0, Math.min(data.a.length(), 3)));
                        }
                        break;
                    case "lifepollution":
                        Object[] columns9 = {"Country Name", "Life Expectancy", "Pollution"};
                        List<Cell> list9 = lifePollution();
                        model.setColumnIdentifiers(columns9);
                        model.setRowCount(0);
                        xaxis = "Life";
                        yaxis = "Pollution";
                        for (Cell data : list9) {
                            model.addRow(new Object[]{data.a, data.b, data.c});
                            ds.add(Double.parseDouble(data.b), Double.parseDouble(data.c), data.a.substring(0, Math.min(data.a.length(), 3)));
                        }
                        break;
                    case "lifeimmigration":
                        Object[] columns10 = {"Country Name", "Life Expectancy", "Immigration"};
                        List<Cell> list10 = lifeImmigration();
                        model.setColumnIdentifiers(columns10);
                        model.setRowCount(0);
                        xaxis = "Life";
                        yaxis = "Immigration";
                        for (Cell data : list10) {
                            model.addRow(new Object[]{data.a, data.b, data.c});
                            ds.add(Double.parseDouble(data.b), Double.parseDouble(data.c), data.a.substring(0, Math.min(data.a.length(), 3)));
                        }
                        break;
                    case "immigrationpopulation":
                        Object[] columns11 = {"Country Name", "Population", "Immigration"};
                        List<Cell> list11 = populationImmigration();
                        model.setColumnIdentifiers(columns11);
                        model.setRowCount(0);
                        xaxis = "Immigration";
                        yaxis = "Population";
                        for (Cell data : list11) {
                            model.addRow(new Object[]{data.a, data.b, data.c});
                            ds.add(Double.parseDouble(data.c), Double.parseDouble(data.b), data.a.substring(0, Math.min(data.a.length(), 3)));
                        }
                        break;
                    case "gdppollution":
                        Object[] columns13 = {"Country Name", "Pollution", "GDP" };
                        List<Cell> list13 = gdpPollution();
                        model.setColumnIdentifiers(columns13);
                        model.setRowCount(0);
                        xaxis = "Pollution";
                        yaxis = "GDP";
                        for (Cell data : list13) {
                            model.addRow(new Object[]{data.a, data.b, data.c});
                            ds.add(Double.parseDouble(data.b), Double.parseDouble(data.c), data.a.substring(0, Math.min(data.a.length(), 3)));
                        }
                        break;
                    case "gdpimmigration":
                        Object[] columns14 = {"Country Name", "GDP", "Immigration" };
                        List<Cell> list14 = gdpImmigration();
                        model.setColumnIdentifiers(columns14);
                        model.setRowCount(0);
                        xaxis = "GDP";
                        yaxis = "Immigration";
                        for (Cell data : list14) {
                            model.addRow(new Object[]{data.a, data.b, data.c});
                            ds.add(Double.parseDouble(data.b), Double.parseDouble(data.c), data.a.substring(0, Math.min(data.a.length(), 3)));
                        }
                        break;
                    case "gdppopulation":
                        Object[] columns15 = {"Country Name", "GDP", "Population" };
                        List<Cell> list15 = gdpPopulation();
                        model.setColumnIdentifiers(columns15);
                        model.setRowCount(0);
                        xaxis = "GDP";
                        yaxis = "Population";
                        for (Cell data : list15) {
                            model.addRow(new Object[]{data.a, data.b, data.c});
                            ds.add(Double.parseDouble(data.b), Double.parseDouble(data.c), data.a.substring(0, Math.min(data.a.length(), 3)));
                        }
                        break;
                    case "pollutionpopulation":
                        Object[] columns12 = {"Country Name", "Population", "Pollution"};
                        List<Cell> list12 = pollutionPopulation();
                        model.setColumnIdentifiers(columns12);
                        model.setRowCount(0);
                        yaxis = "Pollution";
                        xaxis = "Population";
                        for (Cell data : list12) {
                            model.addRow(new Object[]{data.a, data.b, data.c});
                            ds.add(Double.parseDouble(data.b), Double.parseDouble(data.c), data.a.substring(0, Math.min(data.a.length(), 3)));
                        }
                        break;
                    case "pollutionimmigration":
                        Object[] columns16 = {"Country Name", "Pollution", "Immigration"};
                        List<Cell> list16 = pollutionImmigration();
                        model.setColumnIdentifiers(columns16);
                        model.setRowCount(0);
                        yaxis = "Immigration";
                        xaxis = "Pollution";
                        for (Cell data : list16) {
                            model.addRow(new Object[]{data.a, data.b, data.c});
                            ds.add(Double.parseDouble(data.b), Double.parseDouble(data.c), data.a.substring(0, Math.min(data.a.length(), 3)));
                        }
                        break;

                    default:
                        Object[] columns1 = {"Country Name", "Population", "GDP", "Happiness Score", "Life Expectancy", "Pollution", "Immigration Count"};
                        List<Cell> list1 = Query1(query.getText());
                        model.setColumnIdentifiers(columns1);
                        model.setRowCount(0);
                        for (Cell data : list1) {
                            model.addRow(new Object[]{data.a, data.b, data.c, data.d, data.e, data.f, data.g});
                        }
                        query.setText("");
                        break;
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                NumberAxis domain = new NumberAxis(xaxis);
                NumberAxis range = new NumberAxis(yaxis);
                domain.setAutoRangeIncludesZero(false);
                XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
                renderer.setBaseItemLabelGenerator(new LabelGenerator());
                renderer.setBaseItemLabelPaint(Color.green.darker());
                renderer.setBasePositiveItemLabelPosition(
                        new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER));
                renderer.setBaseItemLabelFont(
                        renderer.getBaseItemLabelFont().deriveFont(14f));
                renderer.setBaseItemLabelsVisible(true);
                renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
                XYPlot plot = new XYPlot(ds, domain, range, renderer);
                JFreeChart chart = new JFreeChart(
                        xaxis + " " + yaxis, JFreeChart.DEFAULT_TITLE_FONT, plot, false);


                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));

                ChartFrame cframe = new ChartFrame("Analysis of World Development indicators with Semantic Web", chart);
                cframe.setVisible(true);
                cframe.setSize(1200, 700);

            }
        });

        frame.setSize(3000, 1000);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    public static List<Cell> arrangeCells(List<Cell> list) {
        Collections.sort(list, new Comparator<Cell>() {
            @Override
            public int compare(Cell o1, Cell o2) {
                return Integer.parseInt(o1.b) - Integer.parseInt(o2.b);
            }
        });
        return list;
    }

    public static List<Cell> arrangeCellsDouble(List<Cell> list) {
        Collections.sort(list, new Comparator<Cell>() {
            @Override
            public int compare(Cell o1, Cell o2) {
                Double val = Double.parseDouble(o1.b) - Double.parseDouble(o2.b);
                return val.intValue();
            }
        });
        return list;
    }

    static List<Cell> Query1(String countryname) {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName '" + countryname + "' ." +
                " ?Country ns0:withImmigration ?Immigration ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Country ns0:hasPopulation ?Population ." +
                " OPTIONAL {" +
                " ?Country ns0:hasHVisa ?HVisa ." +
                " ?HVisa ns0:Certified ?certH ." +
                "}" +
                " OPTIONAL {" +
                " ?Country ns0:hasAVisa ?AVisa ." +
                " ?AVisa ns0:Certified ?certA ." +
                "}" +
                " OPTIONAL {" +
                " ?Country ns0:hasBVisa ?BVisa ." +
                " ?BVisa ns0:Certified ?certB ." +
                "}" +
                " OPTIONAL {" +
                " ?Country ns0:hasEVisa ?EVisa ." +
                " ?EVisa ns0:Certified ?certE ." +
                "}" +
                " OPTIONAL {" +
                " ?Country ns0:hasFVisa ?FVisa ." +
                " ?FVisa ns0:Certified ?certF ." +
                "}" +
                " ?Immigration ns0:ImmigrantsCount ?imgcount ." +
                " ?Happiness ns0:GDP ?gdp ." +
                " ?Happiness ns0:HappinessScore ?hscore ." +
                " ?Happiness ns0:LifeExpectancy ?lifexp ." +
                " ?Population ns0:Population ?population ." +
                " ?Happiness ns0:Pollution ?pollution ." +
                "} ";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        List<Cell> list = new ArrayList<>();
        try {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();

                RDFNode immigrationCount = soln.get("imgcount");
                RDFNode gdp = soln.get("gdp");
                RDFNode hscore = soln.get("hscore");
                RDFNode lifexp = soln.get("lifexp");
                RDFNode pollution = soln.get("pollution");
                RDFNode population = soln.get("population");

                Cell cell = new Cell(countryname, population.toString(), gdp.toString(), hscore.toString(), lifexp.toString(), pollution.toString(), immigrationCount.toString());
                list.add(cell);
            }
            return list;
        } finally {
            qexec.close();
        }
    }


    static List<Cell> happinessPollution() {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName ?countryname ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Country ns0:hasPopulation ?Population ." +
                " ?Happiness ns0:HappinessScore ?happinessc ." +
                " ?Population ns0:Population ?population ." +
                " ?Happiness ns0:Pollution ?pollution ." +
                "} " +
                " ORDER BY" +
                " ?happinessc";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            List<Cell> list = new ArrayList<>();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();

                RDFNode node = soln.get("population");
                RDFNode node1 = soln.get("countryname");
                RDFNode node5 = soln.get("population");
                RDFNode pollution = soln.get("pollution");
                RDFNode node6 = soln.get("immcount");
                RDFNode node7 = soln.get("happinessc");

                Double db = Double.parseDouble(node6.toString()) / Double.parseDouble(node.toString()) * 100;

                list.add(new Cell(node1.toString(), node7.toString(), pollution.toString()));

                System.out.println(node1.toString() + " \t " + node.toString() +
                        " \t " + node5.toString() + " \t " + node6.toString() + " \t " + node7.toString());
                //}
            }
            return list;
        } finally {
            qexec.close();
        }
    }

    static List<Cell> happinessPopulation() {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName ?countryname ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Country ns0:hasPopulation ?Population ." +
                " ?Happiness ns0:HappinessScore ?happinessc ." +
                " ?Population ns0:Population ?population ." +
                " ?Immigration ns0:ImmigrantsCount ?immcount ." +
                " ?Happiness ns0:HappinessScore ?happinessc ." +
                "} " +
                " ORDER BY" +
                " ?happinessc";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        List<Cell> list = new ArrayList<>();
        try {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();

                RDFNode countryname = soln.get("countryname");
                RDFNode happiness = soln.get("happinessc");
                RDFNode population = soln.get("population");

                if (!countryname.toString().equals("..") && !happiness.toString().equals("..") && !population.toString().equals("..")) {
                    list.add(new Cell(countryname.toString(), happiness.toString(), population.toString()));
                }

            }
            return list;
        } finally {
            qexec.close();
        }

    }

    static List<Cell> happinessGdp() {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName ?countryname ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Country ns0:withImmigration ?Immigration ." +
                " ?Country ns0:hasPopulation ?Population ." +
                " ?Happiness ns0:HappinessScore ?happinessc ." +
                " ?Population ns0:Population ?population ." +
                " ?Happiness ns0:GDP ?gdp ." +
                "} " +
                " ORDER BY" +
                " ?happinessc";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        List<Cell> list = new ArrayList<>();
        try {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();

                RDFNode countryname = soln.get("countryname");
                RDFNode happiness = soln.get("happinessc");
                RDFNode gdp = soln.get("gdp");

                if (!countryname.toString().equals("..") && !happiness.toString().equals("..") && !gdp.toString().equals("..")) {
                    list.add(new Cell(countryname.toString(), happiness.toString(), gdp.toString()));
                }

            }
            return list;
        } finally {
            qexec.close();
        }
    }

    static List<Cell> happinessImmigration() {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName ?countryname ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Country ns0:withImmigration ?Immigration ." +
                " ?Happiness ns0:HappinessScore ?happinessc ." +
                " ?Immigration ns0:ImmigrantsCount ?immcount ." +
                " ?Happiness ns0:HappinessScore ?happinessc ." +
                "} " +
                " ORDER BY" +
                " ?happinessc";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        List<Cell> list = new ArrayList<>();
        try {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();

                RDFNode countryname = soln.get("countryname");
                RDFNode happiness = soln.get("happinessc");
                RDFNode immigration = soln.get("immcount");

                if (!countryname.toString().equals("..") && !happiness.toString().equals("..") && !immigration.toString().equals("..")) {
                    list.add(new Cell(countryname.toString(), happiness.toString(), immigration.toString()));
                }

            }
            return list;
        } finally {
            qexec.close();
        }
    }

    static List<Cell> happinessLife() {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName ?countryname ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Country ns0:withImmigration ?Immigration ." +
                " ?Country ns0:hasPopulation ?Population ." +
                " ?Happiness ns0:HappinessScore ?happinessc ." +
                " ?Population ns0:Population ?population ." +
                " ?Immigration ns0:ImmigrantsCount ?immcount ." +
                " ?Country ns0:hasHVisa ?HVisa ." +
                " ?HVisa ns0:Certified ?certH ." +
                " ?HVisa ns0:Denied ?deniedH ." +
                " ?Happiness ns0:GDP ?gdp ." +
                " ?Happiness ns0:HappinessScore ?happinessc ." +
                " ?Happiness ns0:LifeExpectancy ?lifexp ." +
                "} " +
                " ORDER BY" +
                " ?happinessc";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        List<Cell> list = new ArrayList<>();
        try {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();

                RDFNode countryname = soln.get("countryname");
                RDFNode happiness = soln.get("happinessc");
                RDFNode lifexp = soln.get("lifexp");


                if (!countryname.toString().equals("..") && !happiness.toString().equals("..") && !lifexp.toString().equals("..")) {
                    list.add(new Cell(countryname.toString(), happiness.toString(), lifexp.toString()));
                }

            }
            return list;
        } finally {
            qexec.close();
        }
    }

    static List<Cell> lifePopulation() {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName ?countryname ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Country ns0:withImmigration ?Immigration ." +
                " ?Country ns0:hasPopulation ?Population ." +
                " ?Happiness ns0:HappinessScore ?happinessc ." +
                " ?Population ns0:Population ?population ." +
                " ?Immigration ns0:ImmigrantsCount ?immcount ." +
                " ?Country ns0:hasHVisa ?HVisa ." +
                " ?HVisa ns0:Certified ?certH ." +
                " ?HVisa ns0:Denied ?deniedH ." +
                " ?Happiness ns0:HappinessScore ?happinessc ." +
                " ?Happiness ns0:LifeExpectancy ?lifexp ." +
                "} " +
                " ORDER BY" +
                " ?lifexp";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        List<Cell> list = new ArrayList<>();
        try {
            ResultSet results = qexec.execSelect();
            List vars = results.getResultVars();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();

                RDFNode countryname = soln.get("countryname");
                RDFNode lifexp = soln.get("lifexp");
                RDFNode population = soln.get("population");

                if (!countryname.toString().equals("..") && !lifexp.toString().equals("..") && !population.toString().equals("..")) {
                    list.add(new Cell(countryname.toString(), lifexp.toString(), population.toString()));
                }

            }
            return list;
        } finally {
            qexec.close();
        }

    }

    static List<Cell> lifePollution() {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName ?countryname ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Country ns0:hasPopulation ?Population ." +
                " ?Population ns0:Population ?population ." +
                " ?Happiness ns0:Pollution ?pollution ." +
                " ?Happiness ns0:LifeExpectancy ?lifexp ." +
                "} " +
                " ORDER BY" +
                " ?lifexp";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        List<Cell> list = new ArrayList<>();
        try {
            ResultSet results = qexec.execSelect();
            List vars = results.getResultVars();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();

                RDFNode countryname = soln.get("countryname");
                RDFNode pollution = soln.get("pollution");
                RDFNode lifexp = soln.get("lifexp");

                if (!countryname.toString().equals("..") && !pollution.toString().equals("..") && !lifexp.toString().equals("..")) {
                    list.add(new Cell(countryname.toString(), lifexp.toString(), pollution.toString()));
                }
                System.out.println(countryname.toString() + pollution.toString() + pollution.toString());
                //}
            }
            return list;
        } finally {
            qexec.close();
        }

    }


    static List<Cell> lifeGdp() {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName ?countryname ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Country ns0:hasPopulation ?Population ." +
                " ?Population ns0:Population ?population ." +
                " ?Happiness ns0:GDP ?gdp ." +
                " ?Happiness ns0:Pollution ?pollution ." +
                " ?Happiness ns0:LifeExpectancy ?lifexp ." +
                "} " +
                " ORDER BY" +
                " ?lifexp";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        List<Cell> list = new ArrayList<>();
        try {
            ResultSet results = qexec.execSelect();
            List vars = results.getResultVars();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();

                RDFNode country = soln.get("countryname");
                RDFNode lifexp = soln.get("lifexp");
                RDFNode gdp = soln.get("gdp");

                if (!country.toString().equals("..") && !lifexp.toString().equals("..") && !gdp.toString().equals("..")) {
                    list.add(new Cell(country.toString(), lifexp.toString(), gdp.toString()));
                }

            }
            return list;
        } finally {
            qexec.close();
        }

    }

    static List<Cell> lifeImmigration() {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName ?countryname ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Happiness ns0:GDP ?gdp ." +
                " ?Happiness ns0:Pollution ?pollution ." +
                " ?Country ns0:withImmigration ?Immigration ." +
                " ?Immigration ns0:ImmigrantsCount ?immcount ." +
                " ?Country ns0:hasPopulation ?Population ." +
                " ?Population ns0:Population ?population ." +
                " ?Happiness ns0:HappinessScore ?hscore ." +
                " ?Happiness ns0:LifeExpectancy ?lifexp ." +
                "} " +
                " ORDER BY" +
                " ?lifexp";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        List<Cell> list = new ArrayList<>();
        try {
            ResultSet results = qexec.execSelect();
            List vars = results.getResultVars();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode countryname = soln.get("countryname");
                RDFNode lifexp = soln.get("lifexp");
                RDFNode immcount = soln.get("immcount");


                if (!countryname.toString().equals("..") && !lifexp.toString().equals("..") && !immcount.toString().equals("..")) {
                    list.add(new Cell(countryname.toString(), lifexp.toString(), immcount.toString()));
                }

            }
            return list;
        } finally {
            qexec.close();
        }
    }

    static List<Cell> populationImmigration() {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName ?countryname ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Happiness ns0:GDP ?gdp ." +
                " ?Happiness ns0:Pollution ?pollution ." +
                " ?Country ns0:withImmigration ?Immigration ." +
                " ?Immigration ns0:ImmigrantsCount ?immcount ." +
                " ?Country ns0:hasPopulation ?Population ." +
                " ?Population ns0:Population ?population ." +
                " ?Happiness ns0:HappinessScore ?hscore ." +
                " ?Happiness ns0:LifeExpectancy ?lifexp ." +
                "} " +
                " ORDER BY" +
                " ?lifexp";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        List<Cell> list = new ArrayList<>();
        try {
            ResultSet results = qexec.execSelect();
            List vars = results.getResultVars();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode countryname = soln.get("countryname");
                RDFNode population = soln.get("population");
                RDFNode immcount = soln.get("immcount");


                if (!countryname.toString().equals("..") && !population.toString().equals("..") && !immcount.toString().equals("..")) {
                    list.add(new Cell(countryname.toString(), population.toString(), immcount.toString()));
                }
            }
            return arrangeCells(list);
        } finally {
            qexec.close();
        }
    }

    static List<Cell> pollutionPopulation() {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName ?countryname ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Happiness ns0:GDP ?gdp ." +
                " ?Happiness ns0:Pollution ?pollution ." +
                " ?Country ns0:withImmigration ?Immigration ." +
                " ?Immigration ns0:ImmigrantsCount ?immcount ." +
                " ?Country ns0:hasPopulation ?Population ." +
                " ?Population ns0:Population ?population ." +
                " ?Happiness ns0:HappinessScore ?hscore ." +
                " ?Happiness ns0:LifeExpectancy ?lifexp ." +
                "} " +
                " ORDER BY" +
                " ?lifexp";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        List<Cell> list = new ArrayList<>();
        try {
            ResultSet results = qexec.execSelect();
            List vars = results.getResultVars();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode countryname = soln.get("countryname");
                RDFNode population = soln.get("population");
                RDFNode pollution = soln.get("pollution");


                if (!countryname.toString().equals("..") && !population.toString().equals("..") && !pollution.toString().equals("..")) {
                    list.add(new Cell(countryname.toString(), population.toString(), pollution.toString()));
                }
            }
            return arrangeCellsDouble(list);
        } finally {
            qexec.close();
        }

    }

    static List<Cell> gdpPollution() {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName ?countryname ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Country ns0:hasPopulation ?Population ." +
                " ?Population ns0:Population ?population ." +
                " ?Happiness ns0:GDP ?gdp ." +
                " ?Happiness ns0:Pollution ?pollution ." +
                " ?Happiness ns0:LifeExpectancy ?lifexp ." +
                "} " +
                " ORDER BY" +
                " ?lifexp";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        List<Cell> list = new ArrayList<>();
        try {
            ResultSet results = qexec.execSelect();
            List vars = results.getResultVars();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();

                RDFNode country = soln.get("countryname");
                RDFNode pollution = soln.get("pollution");
                RDFNode gdp = soln.get("gdp");

                if (!country.toString().equals("..") && !gdp.toString().equals("..") && !pollution.toString().equals("..")) {
                    list.add(new Cell(country.toString(), pollution.toString(), gdp.toString()));
                }

            }
            arrangeCellsDouble(list);
            return list;
        } finally {
            qexec.close();
        }

    }

    static List<Cell> gdpImmigration() {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName ?countryname ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Happiness ns0:GDP ?gdp ." +
                " ?Happiness ns0:Pollution ?pollution ." +
                " ?Country ns0:withImmigration ?Immigration ." +
                " ?Immigration ns0:ImmigrantsCount ?immcount ." +
                " ?Country ns0:hasPopulation ?Population ." +
                " ?Population ns0:Population ?population ." +
                " ?Happiness ns0:HappinessScore ?hscore ." +
                " ?Happiness ns0:LifeExpectancy ?lifexp ." +
                "} " +
                " ORDER BY" +
                " ?lifexp";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        List<Cell> list = new ArrayList<>();
        try {
            ResultSet results = qexec.execSelect();
            List vars = results.getResultVars();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode countryname = soln.get("countryname");
                RDFNode gdp = soln.get("gdp");
                RDFNode immcount = soln.get("immcount");


                if (!countryname.toString().equals("..") && !gdp.toString().equals("..") && !immcount.toString().equals("..")) {
                    list.add(new Cell(countryname.toString(), gdp.toString(), immcount.toString()));
                }
            }
            return arrangeCellsDouble(list);
        } finally {
            qexec.close();
        }
    }

    static List<Cell> gdpPopulation() {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName ?countryname ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Happiness ns0:GDP ?gdp ." +
                " ?Happiness ns0:Pollution ?pollution ." +
                " ?Country ns0:withImmigration ?Immigration ." +
                " ?Immigration ns0:ImmigrantsCount ?immcount ." +
                " ?Country ns0:hasPopulation ?Population ." +
                " ?Population ns0:Population ?population ." +
                " ?Happiness ns0:HappinessScore ?hscore ." +
                " ?Happiness ns0:LifeExpectancy ?lifexp ." +
                "} " +
                " ORDER BY" +
                " ?lifexp";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        List<Cell> list = new ArrayList<>();
        try {
            ResultSet results = qexec.execSelect();
            List vars = results.getResultVars();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode countryname = soln.get("countryname");
                RDFNode gdp = soln.get("gdp");
                RDFNode population = soln.get("population");


                if (!countryname.toString().equals("..") && !gdp.toString().equals("..") && !population.toString().equals("..")) {
                    list.add(new Cell(countryname.toString(), gdp.toString(), population.toString()));
                }
            }
            return arrangeCellsDouble(list);
        } finally {
            qexec.close();
        }
    }
    static List<Cell> pollutionImmigration() {
        FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
        Model model = FileManager.get().loadModel("dbi2.ttl");
        String queryString = "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX ns0: <http://www.semanticweb.org/karthikcs/ontologies/2017/10/untitled-ontology-11#> " +
                "SELECT *" +
                "WHERE { " +
                " ?Country ns0:CountryName ?countryname ." +
                " ?Country ns0:withHappiness ?Happiness ." +
                " ?Happiness ns0:GDP ?gdp ." +
                " ?Happiness ns0:Pollution ?pollution ." +
                " ?Country ns0:withImmigration ?Immigration ." +
                " ?Immigration ns0:ImmigrantsCount ?immcount ." +
                " ?Country ns0:hasPopulation ?Population ." +
                " ?Population ns0:Population ?population ." +
                " ?Happiness ns0:HappinessScore ?hscore ." +
                " ?Happiness ns0:LifeExpectancy ?lifexp ." +
                "} " +
                " ORDER BY" +
                " ?lifexp";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        List<Cell> list = new ArrayList<>();
        try {
            ResultSet results = qexec.execSelect();
            List vars = results.getResultVars();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode countryname = soln.get("countryname");
                RDFNode pollution = soln.get("pollution");
                RDFNode immigration = soln.get("immcount");


                if (!countryname.toString().equals("..") && !pollution.toString().equals("..") && !immigration.toString().equals("..")) {
                    list.add(new Cell(countryname.toString(), pollution.toString(), immigration.toString()));
                }
            }
            return arrangeCellsDouble(list);
        } finally {
            qexec.close();
        }
    }

}

