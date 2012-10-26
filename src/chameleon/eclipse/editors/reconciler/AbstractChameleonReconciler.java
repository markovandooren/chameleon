package chameleon.eclipse.editors.reconciler;

import java.util.concurrent.Semaphore;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.Assert;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextInputListener;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

import chameleon.eclipse.editors.ChameleonDocument;




/**
 * @author Jef Geerinckx
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * Abstract implementation of {@link IReconciler}. The reconciler
 * listens to input document changes as well as changes of
 * the input document of the text viewer it is installed on. Depending on 
 * its configuration it manages the received change notifications in a 
 * queue folding neighboring or overlapping changes together. The reconciler
 * processes the dirty regions as a background activity after having waited for further
 * changes for the configured duration of time. A reconciler is started using the
 * {@link #install(ITextViewer)} method.  As a first step {@link #initialProcess()} is
 * executed in the background. Then, the reconciling thread waits for changes that
 * need to be reconciled. A reconciler can be resumed by calling {@link #forceReconciling()}
 * independent from the existence of actual changes. This mechanism is for subclasses only.
 * It is the clients responsibility to stop a reconciler using its {@link #uninstall()}
 * method. Unstopped reconcilers do not free their resources.
 * <p>
 * It is subclass responsibility to specify how dirty regions are processed.
 * </p>
 * 
 * @see org.eclipse.jface.text.IDocumentListener
 * @see org.eclipse.jface.text.ITextInputListener
 * @see org.eclipse.jface.text.reconciler.DirtyRegion
 * @since 2.0
 */
abstract public class AbstractChameleonReconciler implements IReconciler {

	private static int count=0;

	/**
	 * Background thread for the reconciling activity.
	 */
	class BackgroundThread extends Thread {

		/** Has the reconciler been canceled. */
		private boolean fCanceled= false;
		/** Has the reconciler been reset. */
		private boolean fReset= false;
		/** Some changes need to be processed. */
		private boolean fIsDirty= false;
		/** Is a reconciling strategy active. */
		private boolean fIsActive= false;

		/**
		 * Creates a new background thread. The thread 
		 * runs with minimal priority.
		 *
		 * @param name 
		 * 	the thread's name
		 */
		public BackgroundThread(String name) {
			super(name);
			setPriority(Thread.MIN_PRIORITY);
			setDaemon(true);
			number = ++count;
		}


		/**
		 * Returns whether a reconciling strategy is active right now.
		 *
		 * @return <code>true</code> if a activity is active
		 */
		public boolean isActive() {
			return fIsActive;
		}

		/**
		 * Returns whether some changes need to be processed.
		 * 
		 * @return <code>true</code> if changes wait to be processed
		 * @since 3.0
		 */
		public synchronized boolean isDirty() {
			return fIsDirty;
		}

		/**
		 * Cancels the background thread.
		 */
		public void cancel() {
			fCanceled= true;
			IProgressMonitor pm= fProgressMonitor;
			if (pm != null)
				pm.setCanceled(true);
			synchronized (fDirtyRegionQueue) {
				fDirtyRegionQueue.notifyAll();
			}
		}

		/**
		 * Suspends the caller of this method until this background thread has
		 * emptied the dirty region queue.
		 */
		public void suspendCallerWhileDirty() {
			boolean isDirty;
			do {
				synchronized (fDirtyRegionQueue) {
					isDirty= fDirtyRegionQueue.getSize() > 0;
					if (isDirty) {
						try {
							fDirtyRegionQueue.wait();
						} catch (InterruptedException x) {
						}
					}
				}
			} while (isDirty);
		}

		/**
		 * Reset the background thread as the text viewer has been changed,
		 */
		public void reset() {

			if (fDelay > 0) {

				synchronized (this) {
					fIsDirty= true;
					fReset= true;
				}

			} else {

				synchronized (this) {
					fIsDirty= true;
				}

				synchronized (fDirtyRegionQueue) {
					fDirtyRegionQueue.notifyAll();
				}
			}

			reconcilerReset();
		}

		public void acquire() throws InterruptedException {
			_semaphore.acquire();
		}

		public void release() {
			_semaphore.release();
		}

		private Semaphore _semaphore = new Semaphore(1,true);

		public final int number;

		/**
		 * The background activity. Waits until there is something in the
		 * queue managing the changes that have been applied to the text viewer.
		 * Removes the first change from the queue and process it.
		 * <p>
		 * Calls {@link AbstractChameleonReconciler#initialProcess()} on entrance.
		 * </p>
		 */
		public void run() {



			synchronized (fDirtyRegionQueue) {
				try {
					fDirtyRegionQueue.wait(fDelay);
				} catch (InterruptedException x) {
				}
			}

			initialProcess();

			try {
				while (!fCanceled) {
					// Wait if the reconciler is paused.
					try {
						//					System.out.println("### background thread " + AbstractChameleonReconciler.this.getClass().getName() + " requests lock "+number);
						acquire();
						//					System.out.println("### background thread " + AbstractChameleonReconciler.this.getClass().getName() + " has acquired lock "+number);
					} catch (InterruptedException e) {
					}

					synchronized (fDirtyRegionQueue) {
						try {
							fDirtyRegionQueue.wait(fDelay);
						} catch (InterruptedException x) {
						}
					}

					if (fCanceled) {
						//					System.out.println("### background thread " + AbstractChameleonReconciler.this.getClass().getName() + " releases lock "+number);
						release();
						break;
					}

					if (!isDirty()) {
						//					System.out.println("### background thread " + AbstractChameleonReconciler.this.getClass().getName() + " releases lock "+number);
						release();
						continue;
					}

					synchronized (this) {
						if (fReset) {
							fReset= false;
							//						System.out.println("### background thread " + AbstractChameleonReconciler.this.getClass().getName() + " releases lock "+number);
							release();
							continue;
						}
					}

					ChameleonDirtyRegion r= null;
					synchronized (fDirtyRegionQueue) {
						r= fDirtyRegionQueue.removeNextDirtyRegion();
					}

					fIsActive= true;

					if (fProgressMonitor != null)
						fProgressMonitor.setCanceled(false);

					process(r);

					synchronized (fDirtyRegionQueue) {
						if (0 == fDirtyRegionQueue.getSize()) {
							synchronized (this) {
								fIsDirty= fProgressMonitor != null ? fProgressMonitor.isCanceled() : false;
							}
							reconciled();
							fDirtyRegionQueue.notifyAll();
						}
					}

					fIsActive= false;

					//				System.out.println("### background thread " + AbstractChameleonReconciler.this.getClass().getName() + " releases lock "+number);
					release();
				}
			} catch(RuntimeException e) {
				release();
				throw e;
			}
		}
	}

	/**
	 * Internal document listener and text input listener.
	 */
	class Listener implements IDocumentListener, ITextInputListener {

		/**
		 * Default constructor does nothing. Only here such that we can see
		 * where the listener is constructed.
		 */
		public Listener() {

		}

		/*
		 * @see IDocumentListener#documentAboutToBeChanged(DocumentEvent)
		 */
		public void documentAboutToBeChanged(DocumentEvent e) {
			docAboutToBeChanged();
		}

		/*
		 * @see IDocumentListener#documentChanged(DocumentEvent)
		 */
		public void documentChanged(DocumentEvent e) {

			if (!fThread.isDirty()&& fThread.isAlive()) {
				aboutToBeReconciled();
			}

			if (fProgressMonitor != null && fThread.isActive()) {
				fProgressMonitor.setCanceled(true);
			}

			if (fIsIncrementalReconciler) {
				createDirtyRegion(e);
			}

			fThread.reset();

		}

		/*
		 * @see ITextInputListener#inputDocumentAboutToBeChanged(IDocument, IDocument)
		 */
		public void inputDocumentAboutToBeChanged(IDocument oldInput, IDocument newInput) {

			//			if (oldInput == _document) {
			//				
			//				if (_document != null)
			//					_document.removeDocumentListener(this);
			//					
			//				if (fIsIncrementalReconciler) {
			//					fDirtyRegionQueue.purgeQueue();
			//					if (_document != null && _document.getLength() > 0) {
			//						DocumentEvent e= new DocumentEvent(_document, 0, _document.getLength(), null);
			//						createDirtyRegion(e);
			//						
			//						//Cause the reconciling thread to pause until we have set up the new state
			//						fThread.reset();
			//						fThread.suspendCallerWhileDirty();
			//						try {
			//							System.out.println("### inputDocumentAboutToBeChanged requests lock" + fThread.number);
			//							fThread.acquire();
			//							System.out.println("### inputDocumentAboutToBeChanged has acquired lock" + fThread.number);
			//						} catch (InterruptedException e1) {
			//						}
			//					}
			//				}
			//				
			//				_document= null;
			//			}
		}

		/*
		 * @see ITextInputListener#inputDocumentChanged(IDocument, IDocument)
		 */
		public void inputDocumentChanged(IDocument oldInput, IDocument newInput) {
			if (oldInput == _document) {

				if (_document != null)
					_document.removeDocumentListener(this);

				if (fIsIncrementalReconciler) {
					fDirtyRegionQueue.purgeQueue();
					if (_document != null && _document.getLength() > 0) {
						DocumentEvent e= new DocumentEvent(_document, 0, _document.getLength(), null);
						createDirtyRegion(e);

						//Cause the reconciling thread to pause until we have set up the new state
						fThread.reset();
						fThread.suspendCallerWhileDirty();
						try {
							//							System.out.println("### inputDocumentChanged requests lock" + fThread.number);
							fThread.acquire();
							//							System.out.println("### inputDocumentChanged has acquired lock" + fThread.number);
						} catch (InterruptedException e1) {
						}
					}
				}
				_document= null;
			}
			if(newInput instanceof ChameleonDocument) {
				_document = (ChameleonDocument) newInput;
				if (_document == null) {
					return;
				}
				//FIXME Here the new document is set in the reconciler, but if the thread is executed after
				// the aboutToChange method and this one, the strategy is in an invalid state.
				reconcilerDocumentChanged((ChameleonDocument) _document);

				_document.addDocumentListener(this);
				startReconciling();
			}
		}			
	}

	/** Queue to manage the changes applied to the text viewer. */
	private DirtyRegionQueue fDirtyRegionQueue;
	/** The background thread. */
	private BackgroundThread fThread;
	/** Internal document and text input listener. */
	private Listener fListener;
	/** The background thread delay. */
	private int fDelay= 250; 
	/** Are there incremental reconciling strategies? */
	private boolean fIsIncrementalReconciler= true;
	/** The progress monitor used by this reconciler. */
	private IProgressMonitor fProgressMonitor;

	/** The text viewer's document. */
	protected ChameleonDocument _document;
	/** The text viewer */
	protected ITextViewer _textViewer;


	/**
	 * Processes a dirty region. If the dirty region is <code>null</code> the whole
	 * document is consider being dirty. The dirty region is partitioned by the
	 * document and each partition is handed over to a reconciling strategy registered
	 * for the partition's content type.
	 *
	 * @param dirtyRegion the dirty region to be processed
	 */
	abstract protected void process(ChameleonDirtyRegion dirtyRegion);

	/**
	 * Hook called when the document whose contents should be reconciled
	 * has been changed, i.e., the input document of the text viewer this
	 * reconciler is installed on. Usually, subclasses use this hook to 
	 * inform all their reconciling strategies about the change.
	 * 
	 * @param newDocument the new reconciler document
	 */
	abstract protected void reconcilerDocumentChanged(ChameleonDocument newDocument);


	/**
	 * Creates a new reconciler without configuring it.
	 */ 
	protected AbstractChameleonReconciler() {
		super();
	}

	/**
	 * Tells the reconciler how long it should wait for further text changes before
	 * activating the appropriate reconciling strategies.
	 *
	 * @param delay the duration in milliseconds of a change collection period.
	 */
	public void setDelay(int delay) {
		fDelay= delay;
	}

	/**
	 * Tells the reconciler whether any of the available reconciling strategies
	 * is interested in getting detailed dirty region information or just in the
	 * fact the the document has been changed. In the second case, the reconciling 
	 * can not incrementally be pursued.
	 *
	 * @param isIncremental indicates whether this reconciler will be configured with
	 *		incremental reconciling strategies
	 *
	 * @see DirtyRegion
	 * @see IReconcilingStrategy
	 */
	public void setIsIncrementalReconciler(boolean isIncremental) {
		fIsIncrementalReconciler= isIncremental;
	}

	/**
	 * Sets the progress monitor of this reconciler.
	 * 
	 * @param monitor the monitor to be used
	 */
	public void setProgressMonitor(IProgressMonitor monitor) {
		fProgressMonitor= monitor;
	}

	/**
	 * Returns whether any of the reconciling strategies is interested in
	 * detailed dirty region information.
	 * 
	 * @return whether this reconciler is incremental
	 * 
	 * @see IReconcilingStrategy 
	 */
	protected boolean isIncrementalReconciler() {
		return fIsIncrementalReconciler;
	}

	/**
	 * Returns the input document of the text viewer this reconciler is installed on.
	 * 
	 * @return the reconciler document
	 */
	public IDocument getDocument() {
		return _document;
	}

	/**
	 * Returns the text viewer this reconciler is installed on.
	 * 
	 * @return the text viewer this reconciler is installed on
	 */
	protected ITextViewer getTextViewer() {
		return _textViewer;
	}

	/**
	 * Returns the progress monitor of this reconciler.
	 * 
	 * @return the progress monitor of this reconciler
	 */
	protected IProgressMonitor getProgressMonitor() {
		return fProgressMonitor;
	}

	/*
	 * @see IReconciler#install(ITextViewer)
	 */
	public void install(ITextViewer textViewer) {

		Assert.isNotNull(textViewer);
		synchronized (this) {
			if (fThread != null)
				return;
			fThread= new BackgroundThread(getClass().getName());
		}

		_textViewer= textViewer;

		fListener= new Listener();
		_textViewer.addTextInputListener(fListener);

		fDirtyRegionQueue= new DirtyRegionQueue();
	}

	/*
	 * @see IReconciler#uninstall()
	 */
	public void uninstall() {
		if (fListener != null) {

			_textViewer.removeTextInputListener(fListener);
			if (_document != null) _document.removeDocumentListener(fListener);
			fListener= null;

			synchronized (this) {
				// http://dev.eclipse.org/bugs/show_bug.cgi?id=19135
				BackgroundThread bt= fThread;
				fThread= null;
				bt.cancel();
			}
		}
	}

	/**
	 * Creates a dirty region for a document event and adds it to the queue.
	 *
	 * @param e the document event for which to create a dirty region
	 */
	private void createDirtyRegion(DocumentEvent e) {

		if (e.getLength() == 0 && e.getText() != null) {
			// Insert
			fDirtyRegionQueue.addDirtyRegion(new ChameleonDirtyRegion(e.getOffset(), e.getText().length(), ChameleonDirtyRegion.INSERT, e.getText()));

		} else if (e.getText() == null || e.getText().length() == 0) {
			// Remove
			fDirtyRegionQueue.addDirtyRegion(new ChameleonDirtyRegion(e.getOffset(), e.getLength(), ChameleonDirtyRegion.REMOVE, null));

		} else {
			// Replace (Remove + Insert)
			fDirtyRegionQueue.addDirtyRegion(new ChameleonDirtyRegion(e.getOffset(), e.getLength(), ChameleonDirtyRegion.REMOVE, null));
			fDirtyRegionQueue.addDirtyRegion(new ChameleonDirtyRegion(e.getOffset(), e.getText().length(), ChameleonDirtyRegion.INSERT, e.getText()));
		}
	}

	/**
	 * Hook for subclasses which want to perform some
	 * action as soon as reconciliation is needed.
	 * <p>
	 * Default implementation is to do nothing.
	 * </p>
	 * 
	 * @since 3.0
	 */
	protected void aboutToBeReconciled() {

	}


	protected void reconciled() {}

	/**
	 * This method is called on startup of the background activity. It is called only
	 * once during the life time of the reconciler. Clients may reimplement this method.
	 */
	protected void initialProcess() {
	}

	//	/**
	//	 * Forces the reconciler to reconcile the structure of the whole document.
	//	 * Clients may extend this method.
	//	 */
	//	protected void forceReconciling() {
	//		
	//		if (_document != null) {
	//			
	//			if (fIsIncrementalReconciler) {
	//				DocumentEvent e= new DocumentEvent(_document, 0, _document.getLength(), _document.get());
	//				createDirtyRegion(e);
	//			}
	//			
	//			startReconciling();
	//		}
	//	}

	/**
	 * Starts the reconciler to reconcile the queued dirty-regions.
	 * Clients may extend this method.
	 */
	protected synchronized void startReconciling() {
		if (fThread == null)
			return;

		if (!fThread.isAlive()) {
			try {
				fThread.start();
			} catch (IllegalThreadStateException e) {
				// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=40549
				// This is the only instance where the thread is started; since
				// we checked that it is not alive, it must be dead already due
				// to a run-time exception or error. Exit.
			}
		} else {
			fThread.reset();
			//			System.out.println("### startReconciling releases lock" + fThread.number);
			fThread.release();
			//			System.out.println("### startReconciling has released lock"  + fThread.number);
		}
	}

	/**
	 * Hook that is called after the reconciler thread has been reset.
	 */
	protected void reconcilerReset() {
	}


	protected void docAboutToBeChanged(){ }

}
