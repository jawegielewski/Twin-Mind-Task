# Task for TwinMind

## Some screenshots:

![Screenshot_20250726-204957052](https://github.com/user-attachments/assets/c1b6779f-9739-4080-b550-e91c42f72455)

![Screenshot_20250726-193958268_30pr](https://github.com/user-attachments/assets/caca7cc8-345d-44e5-906a-86ba9eb18f01)

![Screenshot_20250726-194028715_30pr](https://github.com/user-attachments/assets/558dfe67-4bdf-45f4-a6bc-3a417e91b672)

![Screenshot_20250726-194034821_30pr](https://github.com/user-attachments/assets/90102ab5-b5c8-4a09-8d34-4e3134006526)

![Screenshot_20250726-194120521_30pr](https://github.com/user-attachments/assets/11c3d31e-9faa-4b22-816f-f4ecbd2c7b56)



## Requirements:<br><br>

  ### User Authentication:<br>
    ● Implement user login functionality.
    ● Support OAuth-based authentication (Google Sign-In recommended, via Firebase Auth).
  ### Google Calendar Integration:<br>
    ● Allow users to connect and sync with their Google Calendar.
    ● Display upcoming events clearly within the app.
  ### Real-time Meeting Transcription:<br>
    ● Provide a simple and intuitive interface enabling users to start audio transcription as meetings begin.
    ● Capture continuous audio input from the device’s microphone.
    ● Transcribe audio into periodic segments (recommended: every 30 seconds).
    ● Implement a robust offline-first transcription mechanism to handle intermittent network connectivity:
      ○ Utilize phone storage for temporary buffering of audio chunks.
      ○ Develop reliable syncing strategies to ensure no transcription data is lost, even when connectivity drops.
      ○ Implement intelligent retry and re-syncing mechanisms when reconnecting.
    ● Use OpenAI Speech-to-Text API or Google Gemini 2.0 Flash for transcription services.
  ### Interactive Transcript Chat:<br>
    ● Allow users to chat interactively with the full meeting transcript both during and after meetings.
    ● Use OpenAI or Google Gemini APIs, taking transcript segments and user queries as input.
    ● Implement streaming responses for interactive, real-time chat.
  ### Automatic Summary Generation:<br>
    ● After meetings, automatically generate concise and structured summaries.
    ● Present clearly segmented meeting notes.
  ### Local and Online Storage:<br>
    ● Implement efficient storage and synchronization between local device storage (SQLite/Room or Jetpack DataStore recommended) and cloud storage.
    ● Ensure seamless synchronization of transcripts and summaries across sessions and devices.
  ### Backend Development:<br>
    ● Create a robust backend capable of:
      ○ Managing OAuth authentication and session handling.
      ○ Processing and temporarily storing audio data and transcripts (use dummy endpoints acceptable for ASR).
      ○ Storing and retrieving meeting data securely for authenticated users.
  ### Error Handling:<br>
    ● Implement comprehensive error management strategies for:
      ○ Authentication failures and OAuth token expiration.
      ○ Audio processing errors and recording interruptions.
      ○ Connectivity issues with Google Calendar and transcription APIs.
      ○ Handling and recovering lost or corrupted audio chunks.
      ○ Ensure robust transition with bluetooth devices route changes, and audio interruptions.
  ### Testing:<br>
    ● Write comprehensive unit tests covering authentication, calendar integration, audio transcription, and summary generation.
    ● Provide a basic load testing outline to showcase backend scalability and reliability.
  ### Design Guidelines:<br>
    ● Design your Android app to closely resemble the intuitive user experience of our existing iOS application.
    ● Ensure clear navigation, minimal user friction, and responsive interactions.
    ● Optimize UI for various Android devices and screen sizes.
