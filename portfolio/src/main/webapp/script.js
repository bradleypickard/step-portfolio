// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

const about = 
    [
        'This is my background.',
        'These are my interests.',
        'These are some of my projects.'
    ]

/**
 * Modifies the 'innertext' of a given element 
 */
function setContentTo(elementID, content) {
  const container = document.getElementById(elementID);
  container.innerText = content;
}

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['That\'s rough buddy', 'My cabbages!', 'Flameo, hotman!', 'Who are you? And what do you want?'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  setContentTo('greeting-container', greeting);
}

/**
 * Selects the contents of the 'About' section.
 */
function setAboutContentTo(section) {
  switch (section) {
    case 'background':
      content = about[0];
      break;
    case 'interests':
      content = about[1];
      break; 
    case 'projects':
      content = about[2];
      break; 
  }
  
  setContentTo('about-me-container', content);
}