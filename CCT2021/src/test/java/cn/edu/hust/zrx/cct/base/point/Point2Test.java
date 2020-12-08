package cn.edu.hust.zrx.cct.base.point;

import cn.edu.hust.zrx.cct.Logger;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCo;
import cn.edu.hust.zrx.cct.advanced.combined.GantryDataBipolarCoUtils;
import cn.edu.hust.zrx.cct.base.line.Trajectory;
import cn.edu.hust.zrx.cct.base.vector.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Point2Test {

    float CCT345_2_SYMMETRY_PX = (6.336646882102519f);
    float CCT345_2_SYMMETRY_PY = (3.035807472522424f);
    float CCT345_2_SYMMETRY_VX = (0.38268343236508984f);
    float CCT345_2_SYMMETRY_VY = (0.9238795325112867f);

    @Test
    void symmetryByLine() {
        float px = 6.336646882102519f;
        float py = 3.035807472522424f;

        Point2 p1 = Point2.create(px, py);

        float spx, spy, project, diagx, diagy, sppx, sppy;
        // 做对称，关于xy平面中的一条线对称，对称线上的点为 (sy_x,sy_y)，对称线的切向为 (sy_xd,sy_yd)
        spx = px - CCT345_2_SYMMETRY_PX;
        spy = py - CCT345_2_SYMMETRY_PY;
        project = spx * CCT345_2_SYMMETRY_VX + spy * CCT345_2_SYMMETRY_VY;
        diagx = 2.0f * CCT345_2_SYMMETRY_VX * project;
        diagy = 2.0f * CCT345_2_SYMMETRY_VY * project;
        sppx = diagx - spx;
        sppy = diagy - spy;
        px = CCT345_2_SYMMETRY_PX + sppx;
        py = CCT345_2_SYMMETRY_PY + sppy;

        Logger.getLogger().info("px = " + px);
        Logger.getLogger().info("py = " + py);


        Point2 p0 = Point2.create(CCT345_2_SYMMETRY_PX, CCT345_2_SYMMETRY_PY);
        Vector2 v = Vector2.create(CCT345_2_SYMMETRY_VX, CCT345_2_SYMMETRY_VY);

        Point2 p2 = Point2.symmetryByLine(p1, p0, v);

        Logger.getLogger().info("p2 = " + p2);


        //////////////////////

        GantryDataBipolarCo.SecondBend secondBend = GantryDataBipolarCo.SecondBend.getDefault();
        Trajectory trajectory2 = GantryDataBipolarCoUtils.getTrajectory2();
        double mid = secondBend.DL2 + secondBend.CCT345_LENGTH + secondBend.GAP3 + secondBend.QS3_LEN / 2;

        Point2 tmid = trajectory2.pointAt(mid);
        Vector2 tmidv = trajectory2.directAt(mid).rotateSelf(Math.PI / 2);
        Logger.getLogger().info("tmid = " + tmid);
        Logger.getLogger().info("tmidv = " + tmidv);
        Point2 p2t = Point2.symmetryByLine(
                p1,
                tmid,
                tmidv
        );

        Logger.getLogger().info("p2t = " + p2t);

    }
}