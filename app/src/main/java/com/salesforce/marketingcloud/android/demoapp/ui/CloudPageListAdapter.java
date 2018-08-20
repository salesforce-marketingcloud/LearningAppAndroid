package com.salesforce.marketingcloud.android.demoapp.ui;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import com.salesforce.marketingcloud.messages.inbox.InboxMessage;
import com.salesforce.marketingcloud.messages.inbox.InboxMessageManager;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * CloudPageListAdapter can be used to create an &quot;inbox&quot; view of CloudPage Messages that have been
 * retrieved from the Marketing Cloud Servers.
 */
public abstract class CloudPageListAdapter extends BaseAdapter
    implements ListAdapter, InboxMessageManager.InboxResponseListener {

  /**
   * Filter display to all CloudPage Messages
   */
  @SuppressWarnings("WeakerAccess") public static final int DISPLAY_ALL = 0;

  /**
   * Filter display to unread CloudPage Messages
   */
  @SuppressWarnings("WeakerAccess") public static final int DISPLAY_UNREAD = 1;

  /**
   * Filter display to read CloudPage Messages
   */
  @SuppressWarnings("WeakerAccess") public static final int DISPLAY_READ = 2;
  private static final String TAG = "CPListAdapter";
  @MessageStatus private int display;
  private List<InboxMessage> allMessages = new ArrayList<>();
  private List<InboxMessage> unreadMessages = new ArrayList<>();
  private List<InboxMessage> readMessages = new ArrayList<>();
  private Handler uiHandler;
  private InboxMessageManager InboxMessageManager;

  /**
   * The CloudPageListAdapter is dependent on an initialized {@link com.salesforce.marketingcloud.MarketingCloudSdk} to gain access to
   * encrypted storage and respond to device, application and SDK lifecycle events.
     *
     * @param InboxMessageManager an initialized instance of {@link InboxMessageManager}
     */
    @SuppressWarnings("WeakerAccess")
    public CloudPageListAdapter(
            @NonNull final InboxMessageManager InboxMessageManager) {
        super();
        this.InboxMessageManager = InboxMessageManager;
        InboxMessageManager.registerInboxResponseListener(this);
        this.uiHandler = new Handler(Looper.getMainLooper());
        refreshAdapterDataLists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        int count = 0;

        switch (this.display) {
            case DISPLAY_ALL:
                count = allMessages.size();
                break;
            case DISPLAY_UNREAD:
                count = unreadMessages.size();
                break;
            case DISPLAY_READ:
                count = readMessages.size();
                break;
        }

        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItem(int position) {
        InboxMessage message = null;
        switch (this.display) {
            case DISPLAY_ALL:
                message = allMessages.get(position);
                break;
            case DISPLAY_UNREAD:
                message = unreadMessages.get(position);
                break;
            case DISPLAY_READ:
                message = readMessages.get(position);
                break;
        }

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Returns the int representation of the filtered list of messages currently being displayed.
     *
     * @return int representation of the displayed messages.  Will be one of {@link
     * CloudPageListAdapter.MessageStatus}
     */
    @SuppressWarnings("WeakerAccess")
    @MessageStatus
    public int getDisplay() {
        return display;
    }

    /**
     * Set whether you want to display all, unread, or read messages
     *
     * @param display as the int representation of the displayed messages.  Will be one of {@link
     *                CloudPageListAdapter.MessageStatus}
     */
    @SuppressWarnings("WeakerAccess")
    public void setDisplay(@MessageStatus int display) {
        Log.d(TAG, "CloudPage changing display from" + this.display + " to " + display);
        if (this.display != display) {
            this.display = display;
            this.notifyDataSetChanged();
        }
    }

    /**
     * {@inheritDoc}
     */
    @CallSuper
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /**
     * Set a message to read status. This method ensures that the internal database is updated.
     *
     * @param message the {@link InboxMessage} to be acted on.
     */
    @SuppressWarnings("WeakerAccess")
    @CallSuper
    public void setMessageRead(
            @NonNull InboxMessage message) {
        InboxMessageManager.setMessageRead(message);
        this.refreshAdapterDataLists();
    }

    //  /**
    //   * Set a message to unread status. This method ensures that the internal database is updated.
    //   *
    //   * @param message the {@link InboxMessage} to be acted on.
    //   */
    //  @SuppressWarnings("WeakerAccess") @CallSuper public void setMessageUnread(
    //      @NonNull InboxMessage message) {
    //    InboxMessageManager.setMessageUnread(message);
    //    this.refreshAdapterDataLists();
    //  }

    /**
     * Sets a message to deleted, so when new messages are downloaded, it won't be re-added.
     *
     * @param message the {@link InboxMessage} to be acted on.
     */
    @SuppressWarnings("WeakerAccess")
    @CallSuper
    public void deleteMessage(
            @NonNull final InboxMessage message) {
        InboxMessageManager.deleteMessage(message);
        this.refreshAdapterDataLists();
    }

    @Override
    public void onInboxMessagesChanged(@NonNull List<InboxMessage> messages) {
        uiHandler.post(new Runnable() {
            public void run() {
                CloudPageListAdapter.this.refreshAdapterDataLists();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable {
        InboxMessageManager.unregisterInboxResponseListener(this);
        super.finalize();
    }

    private void refreshAdapterDataLists() {
        allMessages.clear();
        unreadMessages.clear();
        readMessages.clear();

        try {
            List<InboxMessage> messages = InboxMessageManager.getMessages();
            Log.d(TAG, messages.size() + " CloudPage Messages");

            allMessages.addAll(messages);
            for (InboxMessage message : messages) {
                if (message.read()) {
                    Log.v(TAG, "Adding READ CloudPage message: " + message.subject());
                    readMessages.add(message);
                } else {
                    Log.v(TAG, "Adding UNREAD CloudPage message: " + message.subject());
                    unreadMessages.add(message);
                }
            }
            Log.d(TAG, readMessages.size() + " READ CloudPage Messages");
            Log.d(TAG, unreadMessages.size() + " UNREAD CloudPage Messages");
        } catch (Exception e) {
            Log.e(TAG, "Failed to build our data lists to populate the adapter.", e);
        } finally {
            this.notifyDataSetChanged();
        }
    }

    /**
     * Constrains the Message Display Filter to one of  {@link
     * CloudPageListAdapter#DISPLAY_ALL}, {@link
     * CloudPageListAdapter#DISPLAY_UNREAD}, {@link
     * CloudPageListAdapter#DISPLAY_READ}
     */
    @SuppressWarnings("WeakerAccess")
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DISPLAY_ALL, DISPLAY_UNREAD, DISPLAY_READ})
    public @interface MessageStatus {
    }
}

