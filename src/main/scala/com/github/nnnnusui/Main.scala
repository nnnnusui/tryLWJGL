package com.github.nnnnusui

import org.lwjgl._
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.system._
import java.nio._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._


object HelloWorld {
  def main(args: Array[String]): Unit = {
    System.out.println("Hello LWJGL " + Version.getVersion + "!")
    new HelloWorld().run()
  }
}

class HelloWorld { // The window handle
  private var window = 0L

  def run(): Unit = {
    init()
    loop()
    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)
    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }

  private def init(): Unit = { // Setup an error callback. The default implementation

    { // Initialize GLFW. Most GLFW functions will not work before doing this.
      val successful = glfwInit()
      if (!successful) throw new IllegalStateException("Unable to initialize GLFW")
    }
    glfwPollEvents()

    // Create the window
    window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL)
    if (window == NULL) throw new RuntimeException("Failed to create the GLFW window")
    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
      def foo(window: Long, key: Int, scancode: Int, action: Int, mods: Int) = {
        if(action == GLFW_PRESS)
          println(s"keyboardInput: ${(key, mods)}")
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true) // We will detect this in the rendering loop
      }

      foo(window, key, scancode, action, mods)
    })
    glfwSetJoystickCallback((jid: Int, event: Int)=>{ println(s"joyStickInput: ${(jid, event)}") })

    // Get the thread stack and push a new frame
//    try {
//      val stack = stackPush
//      try {
//        val pWidth = stack.mallocInt(1) // int*
//        val pHeight = stack.mallocInt(1)
//        // Get the window size passed to glfwCreateWindow
//        glfwGetWindowSize(window, pWidth, pHeight)
//        // Get the resolution of the primary monitor
//        val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor)
//        // Center the window
//        glfwSetWindowPos(window, (vidmode.width - pWidth.get(0)) / 2, (vidmode.height - pHeight.get(0)) / 2)
//      } finally if (stack != null) stack.close()
//    } // the stack frame is popped automatically

    // Make the OpenGL context current
    glfwMakeContextCurrent(window)
    // Enable v-sync
    glfwSwapInterval(1)
    // Make the window visible
    glfwShowWindow(window)
  }

  private def loop(): Unit = { // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities
    // Set the clear color
    glClearColor(1.0f, 0.0f, 0.0f, 0.0f)
    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    println(glfwGetJoystickName(GLFW_JOYSTICK_1))
    while ( {

      println(glfwGetJoystickButtons(GLFW_JOYSTICK_1).get())
      !glfwWindowShouldClose(window)
    }) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // clear the framebuffer

      glfwSwapBuffers(window) // swap the color buffers

      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents()
    }
  }
}
