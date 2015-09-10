package net.forkforge.cubemapassembler.fx_gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;

import net.forkforge.cubemapassembler.Area;
import net.forkforge.cubemapassembler.TxPositions;

import org.imgscalr.Scalr;

public class AreaFx extends Canvas implements Area{
	private BufferedImage IMAGE;
	private TxPositions POSITION;
	private FileChooser FILE_CHOOSER = new FileChooser();
        private String TEXT;

	public AreaFx(String text){
		super();
                TEXT=text;
		setOnDragDropped(new EventHandler<DragEvent>(){
			@Override
			public void handle(DragEvent event) {
				Dragboard db=event.getDragboard();
				if(db.hasFiles()){
					File f=db.getFiles().get(0);
					try{
						setImage(f);
					}catch(IOException e){
						e.printStackTrace();
					}
					repaint();
				}
				event.setDropCompleted(true);
				event.consume();
			}
		});
		
		setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				int clicks=event.getClickCount();
				if(clicks>=2){
					FILE_CHOOSER = new FileChooser();
					FILE_CHOOSER.setTitle("Import image");
					File file = FILE_CHOOSER.showOpenDialog(null);
					if(file!=null){
						try{
							setImage(file);
						}catch(IOException e){
							e.printStackTrace();
						}
						repaint();
					}
				}
			}			
		});
	}

	public void repaint() {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				paintComponent(getGraphicsContext2D());
			}
		});
	}
        
        private int percentage(int i,int j) {
            return (i/100)*j;
        }

	private void paintComponent(GraphicsContext g) {
		int width=(int)getWidth();
		int height=(int)getHeight();

		if(IMAGE!=null){
			BufferedImage img=(BufferedImage)resize(IMAGE,width,height);
			g.drawImage(SwingFXUtils.toFXImage(img,null),0,0);
		}else{
			g.setFill(Color.LIGHTSLATEGRAY);
			g.fillRect(0,0,width,height);
			g.setStroke(Color.LIGHTSALMON);
			g.strokeLine(0,0,width,height);
			g.strokeLine(0,height,width,0);
                        g.strokeLine(0,0,0,0);
                        g.strokeLine(0,height/2,width,height/2);
			g.strokeRect(0,0,width,height);
                        //Text box
                        g.fillRect(percentage(width,25),
                                   percentage(height,35),
                                   percentage(width,50),
                                   percentage(height,30));
                        //Text & Re-fill-coloring (??)
                        g.setFill(Color.LIGHTGREEN);
                        g.setFont(Font.font ("Tahoma", 20));
                        g.fillText(TEXT, POSITION.getXY()[0]+percentage(width,50)-TEXT.length()*(g.getFont().getSize()/3.2f), POSITION.getXY()[0]+percentage(height,52));
		}

	}

	public void setImage(File f) throws IOException {
		setImage(ImageIO.read(f));
	}

	public void setImage(BufferedImage img) {
		IMAGE=img;
	}

	public Image resize(BufferedImage image, int width, int height) {
		return Scalr.resize(image,Scalr.Method.SPEED,Scalr.Mode.FIT_EXACT,width,height,Scalr.OP_ANTIALIAS);
	}

	@Override
	public BufferedImage getImage() {
		return IMAGE;
	}

	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	public void setPosition(TxPositions pos) {
		POSITION=pos;
	}

	@Override
	public TxPositions getPosition() {
		return POSITION;
	}



}
