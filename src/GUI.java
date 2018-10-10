import org.osbot.rs07.api.model.NPC;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;


public class GUI extends JDialog
{
    private JDialog mainDialog;
    private boolean started = false;

    //list variable
    private JScrollPane npcListScrollPane;
    private JList npcList;

    //GUI constructor
    public GUI(List<String> localNpcs)
    {
        //create java dialogue
        mainDialog = new JDialog();
        mainDialog.setTitle("Chip Thiever");
        mainDialog.setModal(true);
        mainDialog.setModalityType(ModalityType.APPLICATION_MODAL);

        //create a new j pannel
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
        main.setBorder(new EmptyBorder(20,20,20,20));

        //add main jpanel to dialogue
        mainDialog.getContentPane().add(main);

        //panel for npc selection
        JPanel npcSelectionPannel = new JPanel();
        npcSelectionPannel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel npcSelectionLabel = new JLabel("NPC To Pickpocket:");
        npcSelectionPannel.add(npcSelectionLabel);
        main.add(npcSelectionPannel);

        //new pannel for jlist

        JPanel npcSelectionPannel2 = new JPanel();
        npcSelectionPannel2.setLayout(new FlowLayout(FlowLayout.CENTER));

        //setup scrollpane
        npcList = new JList(localNpcs.toArray());
        npcList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        npcListScrollPane = new JScrollPane(npcList);
        npcListScrollPane.setPreferredSize(new Dimension(120, 80));
        npcSelectionPannel2.add(npcListScrollPane);
        main.add(npcSelectionPannel2);

        //add start button
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Start");
        buttonPanel.add(okButton);
        okButton.addActionListener(e -> {
            started = true;
            close();
        });

        main.add(buttonPanel);

        //pack up the dialog
        mainDialog.pack();
    }

    public String getNpc()
    {
        return (String) npcList.getSelectedValue();
    }

    //on open, make pane visible
    public void open()
    {
        mainDialog.setVisible(true);
    }

    //on dialog close, set panel invisible and deconstruct
    public void close()
    {
        mainDialog.setVisible(false);
        mainDialog.dispose();
    }

    public boolean isStarted()
    {
        return started;
    }
}

//testing only!
enum Tree {
    NORMAL1,
    OAK1,
    WILLOW1,
    NORMAL,
    OAK,
    WILLOW,
    NORMAL2,
    OAK2,
    WILLOW2;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}