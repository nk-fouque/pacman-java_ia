### To disable rendering part, just comment a few lines of code as below:

**inside br.ol.pacman.infra.Display class:**

```Java
private class MainLoop implements Runnable {

        @Override
        public void run() {
            long desiredFrameRateTime = 1000 / 60;
            long currentTime = System.currentTimeMillis();
            long lastTime = currentTime - desiredFrameRateTime;
            long unprocessedTime = 0;
            boolean needsRender = false;
            while (running) {
//                currentTime = System.currentTimeMillis();
//                unprocessedTime += currentTime - lastTime;
//                lastTime = currentTime;
//               
//                while (unprocessedTime >= desiredFrameRateTime) {
//                    unprocessedTime -= desiredFrameRateTime;
                    update();
//                    needsRender = true;
//                }
//               
//                if (needsRender) {
//                    Graphics2D g = (Graphics2D) bs.getDrawGraphics();
//                    g.setBackground(Color.BLACK);
//                    g.clearRect(0, 0, getWidth(), getHeight());
//                    g.scale(game.screenScale.getX(), game.screenScale.getY());
//                    draw(g);
//                    g.dispose();
//                    bs.show();
//                    needsRender = false;
//                }
//                else {
//                    try {
//                        Thread.sleep(1);
//                    } catch (InterruptedException ex) {
//                    }
//                }
            }
        }
       
    }
...
```
