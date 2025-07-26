Task for TwinMind

Screenshots:

![Screenshot_20250726-193357477_30pr](https://github.com/user-attachments/assets/8a00bb05-eb79-4c12-a629-2d7a0accb4b9)

![Screenshot_20250726-193958268_30pr](https://github.com/user-attachments/assets/caca7cc8-345d-44e5-906a-86ba9eb18f01)

![Screenshot_20250726-194028715_30pr](https://github.com/user-attachments/assets/558dfe67-4bdf-45f4-a6bc-3a417e91b672)

![Screenshot_20250726-194034821_30pr](https://github.com/user-attachments/assets/90102ab5-b5c8-4a09-8d34-4e3134006526)

![Screenshot_20250726-194120521_30pr](https://github.com/user-attachments/assets/11c3d31e-9faa-4b22-816f-f4ecbd2c7b56)



Requirements:<br><br>

  User Authentication:<br>
    ● Implement user login functionality.<br>
    ● Support OAuth-based authentication (Google Sign-In recommended, via Firebase Auth).<br><br>
  Google Calendar Integration:<br>
    ● Allow users to connect and sync with their Google Calendar.<br>
    ● Display upcoming events clearly within the app.<br><br>
  Real-time Meeting Transcription:<br>
    ● Provide a simple and intuitive interface enabling users to start audio transcription as meetings begin.<br>
    ● Capture continuous audio input from the device’s microphone.<br>
    ● Transcribe audio into periodic segments (recommended: every 30 seconds).<br>
    ● Implement a robust offline-first transcription mechanism to handle intermittent network connectivity:<br>
      ○ Utilize phone storage for temporary buffering of audio chunks.<br>
      ○ Develop reliable syncing strategies to ensure no transcription data is lost, even when connectivity drops.<br>
      ○ Implement intelligent retry and re-syncing mechanisms when reconnecting.<br>
    ● Use OpenAI Speech-to-Text API or Google Gemini 2.0 Flash for transcription services.<br><br>
  Interactive Transcript Chat:<br>
    ● Allow users to chat interactively with the full meeting transcript both during and after meetings.<br>
    ● Use OpenAI or Google Gemini APIs, taking transcript segments and user queries as input.<br>
    ● Implement streaming responses for interactive, real-time chat.<br><br>
  Automatic Summary Generation:<br>
    ● After meetings, automatically generate concise and structured summaries.<br>
    ● Present clearly segmented meeting notes.<br><br>
  Local and Online Storage:<br>
    ● Implement efficient storage and synchronization between local device storage (SQLite/Room or Jetpack DataStore recommended) and cloud storage.<br>
    ● Ensure seamless synchronization of transcripts and summaries across sessions and devices.<br><br>
  Backend Development:<br>
    ● Create a robust backend capable of:<br>
      ○ Managing OAuth authentication and session handling.<br>
      ○ Processing and temporarily storing audio data and transcripts (use dummy endpoints acceptable for ASR).<br>
      ○ Storing and retrieving meeting data securely for authenticated users.<br><br>
  Error Handling:<br>
  ● Implement comprehensive error management strategies for:<br>
    ○ Authentication failures and OAuth token expiration.<br>
    ○ Audio processing errors and recording interruptions.<br>
    ○ Connectivity issues with Google Calendar and transcription APIs.<br>
    ○ Handling and recovering lost or corrupted audio chunks.<br>
    ○ Ensure robust transition with bluetooth devices route changes, and audio interruptions<br><br>
  Testing:<br>
    ● Write comprehensive unit tests covering authentication, calendar integration, audio transcription, and summary generation.<br>
    ● Provide a basic load testing outline to showcase backend scalability and reliability.<br><br>
  Design Guidelines:<br>
    ● Design your Android app to closely resemble the intuitive user experience of our existing iOS application.<br>
    ● Ensure clear navigation, minimal user friction, and responsive interactions.<br>
    ● Optimize UI for various Android devices and screen sizes.<br>
